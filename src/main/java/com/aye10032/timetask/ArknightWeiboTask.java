package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.utils.HttpUtils;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.timeutil.TimeUtils;
import com.aye10032.utils.weibo.WeiboPost;
import com.aye10032.utils.weibo.WeiboSet;
import com.aye10032.utils.weibo.WeiboSetItem;
import com.aye10032.utils.weibo.WeiboUtils;
import okhttp3.OkHttpClient;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dazo66
 */
public abstract class ArknightWeiboTask extends SubscribableBase {

    public OkHttpClient client = new OkHttpClient();
    private Set<String> postIds = new HashSet<>();
    private static final Pattern pattern = Pattern.compile("\\[img:(([\\w.:/]+))]");
    private static Pattern img_name_pattern = Pattern.compile("\\w+.(png|jpg|gif)");

    public ArknightWeiboTask(Zibenbot zibenbot) {
        super(zibenbot);
        File file = new File(getBot().appDirectory + "\\arknight\\");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public Date getNextTime(Date date) {
        Date ret = new Date();
        ret.setTime(date.getTime() + TimeUtils.MIN * 3L);
        Calendar c = Calendar.getInstance();
        c.setTime(ret);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour < 8 || hour > 22) {
            c.set(Calendar.HOUR_OF_DAY, 8);
            c.set(Calendar.MINUTE, 5);
            if (hour >= 8) {
                c.add(Calendar.DAY_OF_YEAR, 1);
            }
            return c.getTime();
        }
        return ret;
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        client = client.newBuilder().callTimeout(10, TimeUnit.SECONDS)
                .proxy(Zibenbot.getProxy()).build();
        WeiboSet posts = WeiboUtils.getWeiboSet(client, 6279793937L);
        if (postIds.isEmpty()) {
            posts.forEach(post -> postIds.add(post.getId()));
        } else {
            Iterator<WeiboSetItem> postIterator = posts.iterator();
            while (postIterator.hasNext()) {
                WeiboSetItem post = postIterator.next();
                if (postIds.contains(post.getId())) {
                    postIterator.remove();
                } else {
                    postIds.add(post.getId());
                    if (!post.isPerma()) {
                        try {
                            replyAll(recivers, postToUser(WeiboUtils.getWeiboWithPostItem(client, post)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public String postToUser(WeiboPost post) {
        String des = post.getDescription();
        Set<String> imgUrls = getImgUrl(des);
        des = des.replaceAll("\n" + pattern.pattern(), "");
        Set<File> imgFiles = new LinkedHashSet<>();
        imgUrls.forEach(url -> imgFiles.add(downloadImg(url)));
        StringBuilder builder = new StringBuilder(des);
        builder.append("\n");
        imgFiles.forEach(file -> builder.append(getBot().getImg(file)));
        if (post.isPermaLink()) {
            builder.append("\n").append(postToUser(post.getRetweet()));
        }
        return builder.toString();
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

    private String getFileName(String url) {
        Matcher matcher = img_name_pattern.matcher(url);
        matcher.find();
        return getBot().appDirectory + "\\arknight\\" + matcher.group();
    }

    private static Set<String> getImgUrl(String s) {
        Matcher matcher = pattern.matcher(s);
        Set<String> set = new LinkedHashSet<>();
        int i = 0;
        while (matcher.find(i)) {
            set.add(matcher.group(1));
            i = matcher.start() + 1;
        }
        return set;
    }

}
