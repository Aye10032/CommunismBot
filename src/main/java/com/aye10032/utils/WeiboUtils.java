package com.aye10032.utils;

import okhttp3.OkHttpClient;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

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

    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static LinkedHashSet<WeiboPost> getRecent10Post(OkHttpClient client, String weiboToRssUrl) {
        LinkedHashSet<WeiboPost> posts = new LinkedHashSet<>();
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(loadFromWeiboRSS(client, weiboToRssUrl));
            List<Element> items = document.getRootElement().getChild("channel").getChildren("item");
            items.forEach(item -> {
                try {
                    posts.add(buildWeiboPost(item));
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

    private static WeiboPost buildWeiboPost(Element item) throws ParseException {
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
