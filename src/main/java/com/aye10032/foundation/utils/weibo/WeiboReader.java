package com.aye10032.foundation.utils.weibo;

import com.aye10032.bot.Zibenbot;
import com.aye10032.foundation.utils.ExceptionUtils;
import com.aye10032.foundation.utils.HttpUtils;
import com.aye10032.foundation.utils.timeutil.AsynTaskStatus;
import com.aye10032.foundation.utils.timeutil.AsynchronousTaskPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dazo66
 */
@Slf4j
@Service
public class WeiboReader {

    private File cacheDir;

    @Value("${weibo.data.cache.path}")
    private String weiboCachePath;
    @Autowired
    private AsynchronousTaskPool pool;

    @Autowired
    private Zibenbot zibenbot;
    private static final Pattern pattern = Pattern.compile("\\[img:([\\w.:/\\-]+/([\\w.]+))]");
    private static final Pattern IMG_NAME_PATTERN = Pattern.compile("\\w+.(png|jpg|gif)");

    @PostConstruct
    public void init() {
        this.cacheDir = new File(weiboCachePath);
        if (!this.cacheDir.exists()) {
            this.cacheDir.mkdirs();
        }
    }

    public String postToUser(WeiboPost post) {
        AtomicReference<String> des = new AtomicReference<>(String.format("@%s:\n", post.getUserName()) + post.getDescription());
        Set<String> imgUrls = getImgUrl(des.get());
        des.set(des.get().replaceAll(pattern.pattern(), "$2"));
        Set<File> imgFiles = new LinkedHashSet<>();
        List<Runnable> runnables = new ArrayList<>();
        imgUrls.forEach(url -> {
            imgFiles.add(new File(getFileName(url)));
            runnables.add(() -> downloadImg(url));
        });
        AsynTaskStatus status = pool.execute(
                () ->
                        imgFiles.forEach(file -> {
                            String s = des.get();
                            System.out.println(s);
                            s = s.replace(file.getName(), zibenbot.getImg(file));
                            des.set(s);
                            System.out.println(file.getName() + des.get());
                        }),
                runnables.toArray(new Runnable[0]));
        try {
            status.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ret = des.get();
        if (post.isPermaLink()) {
            ret = ret + "\n" + postToUser(post.getRetweet());
        }
        return ret;
    }

    private File downloadImg(String url) {
        String fileName = getFileName(url);
        File outFile = new File(fileName);
        File tempFile = new File(fileName + "_temp" + RandomUtils.nextInt(10000, 99999));
        try {
            if (!outFile.exists()) {
                tempFile.getParentFile().mkdirs();
                tempFile.createNewFile();
                HttpUtils.download(url, tempFile.getAbsolutePath(), Zibenbot.getOkHttpClient());
                tempFile.renameTo(outFile);
                Thread.sleep(500L);
            }
        } catch (Exception e) {
            tempFile.delete();
            outFile.delete();
            log.error("微博图片下载出错：" + ExceptionUtils.printStack(e));
            return null;
        }
        return outFile;
    }

    private String getFileName(String url) {
        Matcher matcher = IMG_NAME_PATTERN.matcher(url);
        matcher.find();
        return cacheDir.getAbsolutePath() + "/" + matcher.group();
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
