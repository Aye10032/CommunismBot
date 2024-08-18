package com.aye10032.bot.timetask;

import com.aye10032.bot.Zibenbot;
import com.aye10032.foundation.utils.ExceptionUtils;
import com.aye10032.foundation.utils.timeutil.Reciver;
import com.aye10032.foundation.utils.timeutil.SubscribableBase;
import com.aye10032.foundation.utils.weibo.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * @author Dazo66
 */
@Service
@Slf4j
public class GenshinWeiboTask extends SubscribableBase {

    private Set<String> postIds = WeiboCacheService.getCacheIds(GenshinWeiboTask.class);
    @Autowired
    private WeiboReader weiboReader;

    @PostConstruct
    public void init() {
        File file = new File(getAppDirectory() + "/genshin/");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public String getName() {
        return "原神微博小助手";
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        OkHttpClient client = Zibenbot.getOkHttpClient();
        WeiboSet posts = WeiboUtils.getWeiboSet(client, 6593199887L);
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
                    try {
                        log.info(String.format("检测到原神新的饼：%s", post.getTitle()));
                        replyAll(recivers, weiboReader.postToUser(WeiboUtils.getWeiboWithPostItem(client, post)));
                    } catch (Exception e) {
                        log.error("获取饼出错：" + ExceptionUtils.printStack(e));
                    }
                }
            }
        }
    }

    @Override
    public String getCron() {
        return "0 0/3 * * * ? ";
    }

    public static String cleanDes(String offWeb) {
        offWeb = offWeb.replaceAll("<span class=\"head-title\">\\s*([\\S\\s]+)\\s*</span>", "");
        offWeb = offWeb.replaceAll("<img src=\"(\\S+)\"/>", "[img:$1]\n");
        offWeb = offWeb.replaceAll("<title>公告</title>", "");
        offWeb = offWeb.replaceAll("\n", "");
        offWeb = offWeb.replaceAll("\t", "");
        offWeb = offWeb.replaceAll("<br/>", "\n");
        offWeb = offWeb.replaceAll("</p>", "\n");
        offWeb = offWeb.replaceAll("<[\\S\\s]+?>", "");
        offWeb = offWeb.replaceAll("\n\n\n", "\n\n");
        offWeb = offWeb.trim();
        return offWeb;
    }
}
