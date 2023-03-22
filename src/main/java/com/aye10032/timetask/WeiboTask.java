package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ExceptionUtils;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.weibo.*;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Dazo66
 */
@Service
public class WeiboTask extends SubscribableBase {
    @Autowired
    private WeiboReader weiboReader;

    @PostConstruct
    public void init() {
        File file = new File(getBot().appDirectory + "/weibo/");
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
        Zibenbot.logInfoStatic("开始检查微博");
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
                        getBot().logInfo(String.format("检测到新的微博：%s", post.getTitle()));
                        replyAll(recivers, weiboReader.postToUser(WeiboUtils.getWeiboWithPostItem(client, post)));
                    } catch (Exception e) {
                        getBot().logWarning("获取微博出错：" + ExceptionUtils.printStack(e));
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
