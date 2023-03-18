package com.aye10032.utils.weibo;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ExceptionUtils;
import com.aye10032.utils.HttpUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.intellij.lang.annotations.MagicConstant;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Dazo66
 */
public class WeiboUtils {

    private static SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
    private static SimpleDateFormat format2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy", Locale.ENGLISH);
    private static JsonParser parser = new JsonParser();
    //https://weibo.com/6279793937/JFqBeFy4X


    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static LinkedHashSet<WeiboPost> getRecentPostDirect(OkHttpClient client, long userId) {
        LinkedHashSet<WeiboPost> retSet = new LinkedHashSet<>();
        String containerid = null;
        try {
            containerid = getContainerid(client, userId, "weibo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (containerid == null) {
            return retSet;
        }
        WeiboSet postIds = null;
        try {
            postIds = getWeiboIdList(client, userId, containerid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (postIds == null) {
            return retSet;
        }
        postIds.forEach(listItem -> {
            try {
                WeiboPost post = getWeiboWithPostItem(client, listItem);
                retSet.add(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return retSet;

    }

    public static WeiboSet getWeiboSet(OkHttpClient client, Long userId) {
        WeiboSet retSet = new WeiboSet();
        String containerid = null;
        try {
            containerid = getContainerid(client, userId, "weibo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (containerid == null) {
            return retSet;
        }
        try {
            retSet = getWeiboIdList(client, userId, containerid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retSet;
    }

    public static WeiboPost getWeiboWithPostItem(OkHttpClient client, WeiboSetItem item) throws IOException, ParseException {
        try {
            /*if (!item.isLongText()) {
                return itemToPost(item);
            }*/
            WeiboPost post = getWeiboWithId(client, item.getId());
            post.setTop(item.getIsTop());
            return post;
        } catch (Exception e) {
            Zibenbot.logWarningStatic(ExceptionUtils.printStack(e));
            return itemToPost(item);
        }

    }

    @NotNull
    private static WeiboPost itemToPost(WeiboSetItem item) {
        WeiboPost post = new WeiboPost();
        post.setId(item.getId());
        post.setDescription(item.getText());
        post.setTitle(item.getTitle());
        post.setLink(String.format("https://weibo.com/%s/%s", item.getUserID(), item.getId()));
        post.setUserName(item.getUserName());
        post.setUserId(item.getUserID());
        post.setTop(item.getIsTop());
        if (item.getRetweet() != null) {
            post.setRetweet(itemToPost(item.getRetweet()));
            post.setPermaLink(String.format("https://weibo.com/%s/%s", item.getRetweet().getUserID(), item.getRetweet().getId()));
        } else {
            post.setPermaLink(post.getLink());
        }
        return post;
    }

    public static WeiboPost getWeiboWithId(OkHttpClient client, String id) throws IOException, ParseException {
        String weiboListUrl = String.format("https://m.weibo.cn/statuses/show?id=%s", id);
        Request weiboListRequest = new Request.Builder()
                .url(weiboListUrl)
                .method("GET", null)
                .header("MWeibo-Pwa", "1")
                .header("Referer", String.format("https://m.weibo.cn/detail/%s", id))
                .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
                .header("X-Requested-With", "XMLHttpRequest")
                .build();
        String string = client.newCall(weiboListRequest).execute().body().string();
        Zibenbot.logWarningStatic(string);
        JsonObject object = parser.parse(string).getAsJsonObject().getAsJsonObject("data");
        WeiboPost post = buildPostFromJsonObject(object);
        return post;
    }

    private static WeiboPost buildPostFromJsonObject(JsonObject object) throws ParseException {
        WeiboPost post = new WeiboPost();
        //Sat Jan 23 17:00:21 +0800 2021
        //created_at
        try {
            post.setPubDate(format2.parse(object.get("created_at").getAsString()));
        } catch (NullPointerException e) {
            // ignore
        }

        post.setId(object.get("id").getAsString());
        StringBuilder builder = new StringBuilder(object.get("text").getAsString());
        if (object.get("pic_num").getAsInt() > 0) {
            JsonArray pics = object.getAsJsonArray("pics");
            pics.forEach(ele -> builder.append("\n")
                    .append(String.format("[img:%s]", ele.getAsJsonObject().getAsJsonObject("large").get("url").getAsString())));
        }
        post.setDescription(cleanString1(builder.toString()));
        post.setLink(String.format("https://weibo.com/%d/%s",
                object.getAsJsonObject("user").get("id").getAsLong(),
                object.get("bid").getAsString()));
        JsonObject retweet = object.getAsJsonObject("retweeted_status");
        if (retweet == null) {
            post.setPermaLink(post.getLink());
        } else {
            post.setPermaLink(String.format("https://weibo.com/%d/%s",
                    object.getAsJsonObject("retweeted_status").getAsJsonObject("user").get("id").getAsLong(),
                    object.getAsJsonObject("retweeted_status").get("bid").getAsString()));
            post.setRetweet(buildPostFromJsonObject(object.getAsJsonObject("retweeted_status")));
        }
        try {
            post.setTitle(object.get("status_title").getAsString());
        } catch (NullPointerException e) {
        }
        post.setUserName(object.getAsJsonObject("user").get("screen_name").getAsString());
        post.setUserId(object.getAsJsonObject("user").get("id").getAsString());
        return post;
    }

    private static String cleanString1(String s) {
        //<a href='/n/霍里奇肉性恋'>@霍里奇肉性恋</a>
        // <a  href="https://m.weibo.cn/p/index?extparam=%E5%8E%9F%E7%A5%9E&containerid=100808fc439dedbb06ca5fd858848e521b8716" data-hide=""><span class='url-icon'><img style='width: 1rem;height: 1rem' src='https://n.sinaimg.cn/photo/5213b46e/20180926/timeline_card_small_super_default.png'></span><span class="surl-text">原神</span></a> <a  href="https://m.weibo.cn/search?containerid=231522type%3D1%26t%3D10%26q%3D%23%E5%8E%9F%E7%A5%9E%23&extparam=%23%E5%8E%9F%E7%A5%9E%23" data-hide=""><span class="surl-text">#原神#</span></a> <br /><br />《原神》2.7版本更新延期补偿说明 <a  href="https://weibo.com/ttarticle/p/show?id=2309404765885876535937" data-hide=""><span class='url-icon'><img style='width: 1rem;height: 1rem' src='https://h5.sinaimg.cn/upload/2015/09/25/3/timeline_card_small_article_default.png'></span><span class="surl-text">《原神》2.7版本更新延期补偿说明</span></a>
        s = s.replaceAll("<a href='/n/([^\\s/ @]+)'>@\\1</a>", "@$1");
        s = s.replaceAll("<a +href=\"(\\S+)\" data-hide=\"\"><span class=\"surl-text\">#(\\S+)#</span></a>", "#$2#");
        s = s.replaceAll(" <a +href=\"(\\S+)\" data-hide=\"\"><span class='url-icon'>", "$1");
        s = s.replaceAll("<img style='[1-9a-zA-Z ;:]+' src='(\\S+)'>", "[img:$1]");
        s = s.replaceAll("<br />", "\n");
        s = s.replaceAll("<br><br>", "\n");
        s = s.replaceAll("<a data-url=\"(\\S+)\" href=\"\\S+\" data-hide=\"\"><span class='url-icon'><img style='([1-9a-zA-Z ;:]+)' src='(\\S+)'></span><span class=\"surl-text\">([^\\s<>]+)</span></a>", "$4: $1\n[img:$3]");
        s = s.replaceAll("<a data-url=\"(\\S+)\" href=\"\\S+\" data-hide=\"\"><span class='url-icon'></span><span class=\"surl-text\">([^\\s<>]+)</span></a>", "$2: $1");
        s = s.replaceAll("<span class=\"url-icon\"><img alt=(\\S+) src=\"\\S+\" style=\"[1-9a-zA-Z ;:]+\" */></span>", "$1");
        s = s.replaceAll("(<[^>\n]*>)?", "");
        //<a data-url="http://t.cn/A65fnJRL" href="https://video.weibo.com/show?fid=1034:4598289648255023" data-hide="">
        //<span class='url-icon'>
        //<img style='width: 1rem;height: 1rem' src='https://h5.sinaimg.cn/upload/2015/09/25/3/timeline_card_small_video_default.png'>
        //</span><span class="surl-text">明日方舟Arknights的微博视频</span></a>
        //s = s.replaceAll("", "");
        return s;

    }

    private static WeiboSet getWeiboIdList(OkHttpClient client, Long userId, String containerid) throws IOException {
        WeiboSet retSet = new WeiboSet();
        String weiboListUrl = String.format("https://m.weibo.cn/api/container/getIndex?type=uid&value=%d&containerid=%s",
                userId,
                containerid);
        Request weiboListRequest = new Request.Builder()
                .url(weiboListUrl)
                .method("GET", null)
                .header("MWeibo-Pwa", "1")
                .header("Referer", String.format("https://m.weibo.cn/u/%d", userId))
                .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
                .header("X-Requested-With", "XMLHttpRequest")
                .build();
        JsonObject object = parser.parse(client.newCall(weiboListRequest).execute().body().string()).getAsJsonObject();
        JsonArray array = object.getAsJsonObject("data").getAsJsonArray("cards");
        array.forEach(ele -> retSet.add(buildItem(ele.getAsJsonObject().getAsJsonObject("mblog"))));
        return retSet;
    }

    private static WeiboSetItem buildItem(JsonObject o) {
        String id = o.get("id").getAsString();
        boolean isTop = false;
        if (o.get("isTop") != null && 1 == o.get("isTop").getAsInt()) {
            isTop = true;
        }
        String title;
        title = getTitle(o.get("text").getAsString());
        WeiboSetItem itme = new WeiboSetItem(id, title, isTop, false);
        String text = cleanString1(o.get("text").getAsString());
        StringBuilder builder = new StringBuilder(text);
        if (o.get("pic_num") != null && o.get("pic_num").getAsInt() > 0) {
            for (JsonElement element : o.getAsJsonArray("pics")) {
                builder.append("\n");
                builder.append(
                        String.format("[img:%s]", element.getAsJsonObject().getAsJsonObject("large").get("url").getAsString()));
            }
        }
        itme.setText(builder.toString());
        JsonElement isLongText = o.get("isLongText");
        itme.setLongText(isLongText == null ? false : isLongText.getAsBoolean());
        itme.setUserID(o.getAsJsonObject("user").get("id").getAsString());
        itme.setUserName(o.getAsJsonObject("user").get("screen_name").getAsString());
        //itme.setPerma();
        if (o.get("retweeted_status") != null) {
                /*title = getTitle(o.getAsJsonObject("retweeted_status").get("text").getAsString());
                if (!title.startsWith("转发微博") && !title.startsWith("Repost")) {
                    title = "转发微博：" + title;
                }*/
            itme.setPerma(true);
            itme.setRetweet(buildItem(o.getAsJsonObject("retweeted_status")));
        }
        return itme;
    }

    private static String getTitle(String text) {
        text = cleanString1(text);
        text = text.replaceAll("\n", "");
        if (text.length() > 30) {
            return text.substring(0, 30) + "...";
        } else {
            return text;
        }
    }

    private static String getContainerid(OkHttpClient client,
                                         Long userId,
                                         @MagicConstant(stringValues = {"profile", "weibo", "original_video", "album"})
                                                 String tabKey) throws IOException {
        String userInfoUrl = String.format("https://m.weibo.cn/api/container/getIndex?type=uid&value=%d", userId);
        Request userInfoRequest = new Request.Builder()
                .url(userInfoUrl)
                .method("GET", null)
                .header("MWeibo-Pwa", "1")
                .header("Referer", String.format("https://m.weibo.cn/u/%d", userId))
                .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
                .header("X-Requested-With", "XMLHttpRequest")
                .build();
        JsonObject object = parser.parse(client.newCall(userInfoRequest).execute().body().string()).getAsJsonObject();
        JsonArray array = object.getAsJsonObject("data").getAsJsonObject("tabsInfo").getAsJsonArray("tabs");
        for (JsonElement ele : array) {
            JsonObject o = ele.getAsJsonObject();
            if (o.get("tabKey").getAsString().equals(tabKey)) {
                return o.get("containerid").getAsString();
            }
        }
        return null;
    }

    public static LinkedHashSet<WeiboPost> getRecent10PostFromRss(OkHttpClient client, String weiboToRssUrl) {
        LinkedHashSet<WeiboPost> posts = new LinkedHashSet<>();
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(loadFromWeiboRSS(client, weiboToRssUrl));
            List<Element> items = document.getRootElement().getChild("channel").getChildren("item");
            items.forEach(item -> {
                try {
                    posts.add(buildWeiboPost1(item));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
        return posts;
    }

    private static String cleanString(String s) {
        s = s.replaceAll("\\!\\[CDATA\\[ ([\\w|\\W]*) ]]", "$1");
        s = s.replaceAll("<a +href=\"(\\S+)\" data-hide=\"\"><span class=\"surl-text\">#明日方舟#</span></a>", "#明日方舟#");
        s = s.replaceAll("<br />", "\n");
        s = s.replaceAll("<br><br>", "\n");
        s = s.replaceAll("<a href=\"([\\w|.|:|/]+)\" target=\"_blank\"><img src=\"\\1\"></a>", "[img:$1]");
        s = s.replaceAll("<a data-url=\"(\\S+)\" href=\"\\S+\" data-hide=\"\"><span class=\"surl-text\">(\\S+)</span></a>", "$2: $1");
        //s = s.replaceAll("", "");
        return s;
    }

    private static WeiboPost buildWeiboPost1(Element item) throws ParseException {
        WeiboPost post = new WeiboPost();
        post.setTitle(cleanString(item.getChildText("title")));
        post.setDescription(cleanString(item.getChildText("description")));
        //Thu, 28 Jan 2021 03:10:04 GMT
        post.setPubDate(format.parse(item.getChildText("pubDate")));
        post.setPermaLink(item.getChildText("guid"));
        post.setLink(item.getChildText("link"));
        return post;
    }

    private static InputStream loadFromWeiboRSS(OkHttpClient client, String weiboToRssUrl) throws IOException {
        return HttpUtils.getInputStreamFromNet(weiboToRssUrl, client);
    }


}
