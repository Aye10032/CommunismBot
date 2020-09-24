package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.functions.ScreenshotFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.*;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.timeutil.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dazo66
 */
public abstract class DragraliaTask extends SubscribableBase {

    public Config config;
    public ConfigLoader<Config> loader;
    Zibenbot zibenbot;
    Gson gson = new Gson();
    OkHttpClient client =
            new OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).proxy(Zibenbot.proxy).build();
    private JsonParser jsonParser = new JsonParser();

    public DragraliaTask(Zibenbot zibenbot) {
        super(zibenbot);
        Proxy proxy;
        if ((proxy = Zibenbot.getProxy()) != null) {
            client =
                    new OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).proxy(proxy).build();
        }
        this.zibenbot = zibenbot;
        loader = new ConfigLoader<>(zibenbot.appDirectory + "/dragralia_4.json", Config.class);
        config = loader.load();
    }

    @Override
    public Date getNextTime(Date date) {
        /*long d = date.getTime();
        //得到下一个01分
        //19：00
        d = d + (TimeUtils.HOUR + TimeUtils.SEC - ((d - TimeUtils.SEC) % TimeUtils.HOUR)) + TimeUtils.SEC;
        return new Date(d);*/
        Date date1 = TimeUtils.getNextSpecialTime(date, -1, -1, -1, 0, 1, 0);
        return date1;
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        client = client.newBuilder().proxy(Zibenbot.getProxy()).build();
        try {
            ArticleUpateDate date = null;
            try {
                date = getUpdateDate();
            } catch (Exception e) {
                e.printStackTrace();
                //zibenbot.replyMsg(cqMsg, "公告获取异常");
            }
            Set<Article> articles = getNewArticles(date);
            articles.forEach(a -> {
                try {
                    List<SimpleMsg> list = new ArrayList<>();
                    recivers.forEach(r -> list.add(r.getSender()));
                    this.sendArticle(a, list);
                } catch (Exception e) {
                    zibenbot.logWarning("发送公告出错：" + ExceptionUtils.printStack(e));
                }
            });
        } catch (Exception e) {
            zibenbot.log(Level.WARNING, ExceptionUtils.printStack(e));
        }
    }

    private synchronized Set<Article> getNewArticles(ArticleUpateDate date) {
        Set<Article> set = new HashSet<>();
        ArticleUpateDate last = gson.fromJson(config.getWithDafault("last_update_date", "{}"),
                ArticleUpateDate.class);
        long last_data = Long.parseLong(config.getWithDafault("last_data",
                Long.toString(System.currentTimeMillis() / 1000 - 86400)));
        long current = System.currentTimeMillis() / 1000;
        //判断是不是过了一天的14点
        if ((current - 21600) / 86400 > (last_data - 21600) / 86400) {
            last.clear();
        }
        date.new_article_list.forEach(i -> {
            if (!last.new_article_list.contains(i)) {
                try {
                    Article article = getArticleFromNet(i, false);
                    if (article != null && article.title_name != null) {
                        set.add(article);
                        last.new_article_list.add(i);
                    } else {
                        throw new RuntimeException("获取公告出错");
                    }
                } catch (Exception e) {
                    zibenbot.logWarning("获取公告出错：" + ExceptionUtils.printStack(e));
                    Article a = new Article();
                    a.message =
                            "更新公告异常\n公告页面：https://dragalialost.com/chs/news/detail/" + a.article_id;
                    a.article_id = -1;
                    set.add(a);
                }
            }
        });
        date.update_article_list.forEach(i -> {
            ArticleUpateDate.UpdateDate d = null;
            for (ArticleUpateDate.UpdateDate date1 : last.update_article_list) {
                if (date1.id == i.id) {
                    d = date1;
                    break;
                }
            }
            if (d == null || d.update_time < i.update_time) {
                try {
                    Article article = getArticleFromNet(i.id, true);
                    if (article != null && article.title_name != null) {
                        set.add(article);
                        last.update_article_list.remove(d);
                        last.update_article_list.add(i);
                    } else {
                        throw new RuntimeException("获取公告出错");
                    }
                } catch (Exception e) {
                    Article a = new Article();
                    a.message =
                            "更新公告异常\n公告页面：https://dragalialost.com/chs/news/detail/" + a.article_id;
                    a.article_id = -1;
                    set.add(a);
                }
            }

        });
        config.set("last_update_date", gson.toJson(last));
        if (set.size() > 0) {
            config.set("last_data", gson.toJson(current));
        }
        loader.save(config);
        return set;

    }

    public void sendArticle(Article a, List<SimpleMsg> simpleMsg) {
        List<String> img_list = new ArrayList<>();
        List<String> img_tag_list = new ArrayList<>();
        List<Runnable> runs = new ArrayList<>();
        if (dragralia_life_pattern.matcher(a.title_name).find()) {
            setDragraliaLifeArticle(a);
        }

        String msg = StringEscapeUtils.unescapeHtml4(a.message);
        Matcher matcher = img_tag_pattern.matcher(msg);
        List<String> matchStrs = new ArrayList<>();
        AtomicReference<File> screenshotFile = new AtomicReference<>();
        int len = Chinese_length(msg);
        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            matchStrs.add(matcher.group());//获取当前匹配的值
        }
        for (String s : matchStrs) {
            img_tag_list.add(s);
            Matcher matcher1 = img_url_pattern.matcher(s);
            if (matcher1.find()) {
                img_list.add(matcher1.group());
            }
        }
        if (len <= 300) {
            img_list.forEach(img -> runs.add(() -> downloadImg(img)));
        } else {
            screenshotFile.set(getScreenshot(a));
        }
        if (!"".equals(a.image_path)) {
            runs.add(() -> downloadImg(a.image_path));
        }
        try {
            zibenbot.pool.getAsynchronousPool().execute(() -> {
                StringBuilder builder = new StringBuilder();
                if (a.article_id != -1) {
                    builder.append("【").append(a.category_name).append("】 ").append(a.title_name).append("\n");
                    if (!"".equals(a.image_path)) {
                        try {
                            builder.append(zibenbot.getImg(new File(getFileName(a.image_path))));
                            builder.append("\n");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (a.isUpdate) {
                        //builder.append("（Update）\n");
                    }
                    String ret = clearMsg(msg);
                    if (len > 300) {
                        if (screenshotFile.get() != null && screenshotFile.get().exists()) {
                            try {
                                builder.append("公告详情：").append("\n").append(zibenbot.getImg(screenshotFile.get()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            builder.append("公告加载失败，请前往官网查看：\n");
                            builder.append("https://dragalialost.com/chs/news/detail/").append(a.article_id);
                        }
                    } else {
                        builder.append(ret);
                    }
                } else {
                    builder.append(a.message);
                }
                //todo 测试完毕修改这里
                send(simpleMsg, builder.toString());
            }, runs.toArray(new Runnable[]{})).wait1();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void send(List<SimpleMsg> list, String s){
        for (SimpleMsg simpleMsg : list) {
            zibenbot.replyMsg(simpleMsg, s);
        }
    }

    private void setDragraliaLifeArticle(Article a) {
        try {
            System.out.println(a.title_name);
            Matcher matcher = Pattern.compile(NUMBER_PATTERN).matcher(a.title_name);
            matcher.find();
            Integer ep_num = Integer.valueOf(matcher.group());
            RequestBody formBody = new FormBody.Builder().add("lang", "chs").add("type",
                    "dragalialife").build();
            String s = IOUtils.toString(HttpUtils.postInputStreamFromNet("https://comic" +
                    ".dragalialost.com/api/index", client, formBody));
            System.out.println(s);
            JsonObject latest_comic =
                    jsonParser.parse(s).getAsJsonObject().get("latest_comic").getAsJsonObject();
            if (latest_comic.get("episode_num").getAsInt() == ep_num) {
                String s1 = dragralia_life.replace("{title_name}",
                        latest_comic.get("title").getAsString());
                s1 = s1.replace("{img_path}", latest_comic.get("main").getAsString());
                a.message = s1;
                a.image_path = latest_comic.get("thumbnail_l").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private File getScreenshot(Article a) {
        String dir;
        if ("test".equals(zibenbot.appDirectory)) {
            dir = "res";
        } else {
            dir = zibenbot.appDirectory;
        }
        String url = "https://dragalialost.com/chs/news/detail/" + a.article_id;
        String outputfile = dir + "/dragraliatemp/" + a.article_id + ".png";
        try {
            WebDriver driver = SeleniumUtils.getDriver();
            return ScreenshotFunc.getScreenshot(driver, url, outputfile, 4000, "for(item of " +
                    "document.getElementsByTagName('details')) {\n" + "    item.open = true;\n" + "}");
        } catch (Exception e) {
            e.printStackTrace();
            zibenbot.logWarning("截图龙约公告出错：" + ExceptionUtils.printStack(e));
        }
        return null;
    }

    private String clearMsg(String msg) {
        Matcher matcher = img_tag_pattern.matcher(msg);
        while (matcher.find()) {
            String tag = matcher.group();
            Matcher matcher1 = img_url_pattern.matcher(tag);
            matcher1.find();
            File file = new File(getFileName(matcher1.group()));
            if (file.exists()) {
                try {
                    msg = msg.replace(tag, zibenbot.getImg(file));
                } catch (Exception e) {
                    msg = msg.replace(tag, "[图片加载错误]");
                }
            } else {
                msg = msg.replace(tag, "[图片加载错误]");
            }
            /*} else {
                msg = msg.replace(img, "[图片]");
            }*/
        }
        msg = replaceDate(msg);
        msg = msg.replace("<br>", "\n");
        msg = msg.replace("</div>", "\n");
        msg = msg.replaceAll("<[^<>]*?>", "");
        msg = msg.replaceAll("[\\t]+", "");
        msg = msg.replaceAll(" +", " ");
        msg = msg.replaceAll("[\n]{3,}", "\n");
        return msg;
    }

    private String replaceDate(String msg) {
        Matcher matcher = date_tag_pattern.matcher(msg);
        List<String> matchStrs = new ArrayList<>();
        DateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            matchStrs.add(matcher.group());//获取当前匹配的值
        }
        for (String s : matchStrs) {
            Matcher matcher1 = date_src_pattern.matcher(s);
            matcher1.find();
            Date date = new Date(Long.valueOf(matcher1.group()) * 1000);
            String s1 = format1.format(date);
            msg = msg.replace(s, s1);
        }
        return msg;
    }

    private String getFileName(String url) {
        Matcher matcher = img_name_pattern.matcher(url);
        matcher.find();
        return zibenbot.appDirectory + "\\dragraliatemp\\" + matcher.group();
    }

    private File downloadImg(String url) {
        File tmpFile = new File(getFileName(url));
        try {
            if (!tmpFile.exists()) {
                tmpFile.getParentFile().mkdirs();
                tmpFile.createNewFile();
                HttpUtils.download(url, tmpFile.getAbsolutePath(), client);
            }
        } catch (Exception e) {
            tmpFile.delete();
            return null;
        }
        return tmpFile;
    }

/*    public Article getArticle(ArticleInfo articleInfo) {
        try {
            InputStream stream = HttpUtils.getInputStreamFromNet("https://dragalialost
            .com/api/index.php?format=json&type=information&category_id=&priority_lower_than
            =&action=information_detail&article_id=" + articleInfo.article_id +
            "&lang=zh_cn&td=%2B08%3A00", client);
            JsonObject object = jsonParser.parse(IOUtils.toString(stream)).getAsJsonObject();
            JsonObject data = object.get("data").getAsJsonObject().get("information")
            .getAsJsonObject();
            return gson.fromJson(data, Article.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public Article getArticleFromNet(int id, boolean isUpdate) throws IOException {
        InputStream stream =
                HttpUtils.getInputStreamFromNet("https://dragalialost.com/api/index" + ".php" +
                        "?format=json&type=information&category_id=&priority_lower_than=&action" + "=information_detail&article_id=" + id + "&lang=zh_cn&td=%2B08%3A00", client);
        JsonParser jsonParser = new JsonParser();
        JsonObject object = jsonParser.parse(IOUtils.toString(stream)).getAsJsonObject();
        JsonElement e = object.get("data").getAsJsonObject().get("information");
        Article a = gson.fromJson(e, Article.class);
        if (isUpdate) {
            a.isUpdate = true;
        }
        return a;
    }

    public ArticleUpateDate getUpdateDate() throws IOException {
        ArticleUpateDate date = new ArticleUpateDate();
        InputStream stream =
                HttpUtils.getInputStreamFromNet("https://dragalialost.com/api/index" + ".php" +
                        "?format=json&type=information&category_id=0&priority_lower_than=&action" + "=information_list&article_id=&lang=zh_cn&td=%2B08%3A00", client);
        JsonParser jsonParser = new JsonParser();
        JsonObject object = jsonParser.parse(IOUtils.toString(stream)).getAsJsonObject();
        object.get("data").getAsJsonObject().get("new_article_list").getAsJsonArray().forEach(jsonElement -> date.new_article_list.add(jsonElement.getAsInt()));
        object.get("data").getAsJsonObject().get("update_article_list").getAsJsonArray().forEach(e -> {
            int id = e.getAsJsonObject().get("id").getAsInt();
            long update_time = e.getAsJsonObject().get("update_time").getAsLong();
            date.update_article_list.add(new ArticleUpateDate.UpdateDate(id, update_time));
        });
        return date;
    }

    private void cleanImg() {
        File dir = new File(zibenbot.appDirectory + "\\dragraliatemp");
        long current = System.currentTimeMillis();
        int i = 0;
        for (File f : dir.listFiles()) {
            if (f.isFile() && current - f.lastModified() > 86400 * 3 * 1000) {
                f.delete();
                i++;
            }
        }
        zibenbot.logInfo("清理了三天前的缓存 " + i + " 张。");
    }

    private static final String NUMBER_PATTERN = "\\d+";
    //<span class="local_date" data-local_date="1587708000">
    private static Pattern date_tag_pattern = Pattern.compile("<span class=\"local_date\" " +
            "data-local_date=\"\\d{10}\">");
    private static Pattern date_src_pattern = Pattern.compile("\\d{10}");
    private static Pattern img_name_pattern = Pattern.compile("\\w+.(png|jpg)");
    private static Pattern img_tag_pattern = Pattern.compile("<img[^<>]*\">");
    private static Pattern img_url_pattern = Pattern.compile("http[^<>]*.(png|jpg)");
    private static Pattern dragralia_life_pattern = Pattern.compile("“轻松龙约”第\\d+话更新");
    private static String dragralia_life = "<div>{title_name}</div><div><br></div><div><img " +
            "src=\"{img_path}\"></div>" + "<div><br></div><div><br></div><div><br></div>" + "<div"
            + ">今后也请继续支持《Dragalia Lost ～失落的龙约～》。</div>";

    public static int Chinese_length(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (!(ascii >= 0 && ascii <= 255)) {
                length += 1;
            }
        }
        return length;
    }

    public static class Article implements Comparable {
        //文章id
        public int article_id;
        //类型名字
        public String category_name;
        //日期
        public String image_path;
        public String message;
        public String next_article_id;
        public String pr_category_id;
        public String prev_article_id;
        public long start_time;
        public String title_name;
        public boolean isUpdate = false;
        public long update_time;

        @Override
        public int hashCode() {
            return article_id * 114514;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Article) {
                return obj.hashCode() == this.hashCode();
            }
            return false;
        }

        @Override
        public int compareTo(Object o) {
            return o.hashCode() - hashCode();
        }
    }

}
