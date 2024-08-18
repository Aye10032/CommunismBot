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
@Slf4j
@Service
public class WeiboTask extends SubscribableBase {
    @Autowired
    private WeiboReader weiboReader;

    @PostConstruct
    public void init() {
        File file = new File(getAppDirectory() + "/weibo/");
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    @Override
    public String getName() {
        return "微博订阅小助手";
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        log.info("开始检查微博");
        if (args == null || args.length == 0) {
            return;
        }
        Long weiboId = Long.valueOf(args[0]);
        OkHttpClient client = Zibenbot.getOkHttpClient();
        WeiboSet posts = WeiboUtils.getWeiboSet(client, weiboId);
        Set<String> cacheWeiboIds = WeiboCacheService.getCacheIds(args[0]);
        if (cacheWeiboIds.isEmpty()) {
            posts.forEach(post -> cacheWeiboIds.add(post.getId()));
        } else {
            Iterator<WeiboSetItem> postIterator = posts.iterator();
            while (postIterator.hasNext()) {
                WeiboSetItem post = postIterator.next();
                if (cacheWeiboIds.contains(post.getId())) {
                    postIterator.remove();
                } else {
                    cacheWeiboIds.add(post.getId());
                    try {
                        log.info(String.format("检测到新的微博：%s", post.getTitle()));
                        replyAll(recivers, weiboReader.postToUser(WeiboUtils.getWeiboWithPostItem(client, post)));
                    } catch (Exception e) {
                        log.error("获取微博出错：" + ExceptionUtils.printStack(e));
                    }
                }
            }
        }
    }

    @Override
    public String getCron() {
        return "0 0/5 * * * ? ";
    }
}
