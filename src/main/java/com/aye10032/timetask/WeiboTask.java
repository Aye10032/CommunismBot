package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ExceptionUtils;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.weibo.WeiboReader;
import com.aye10032.utils.weibo.WeiboSet;
import com.aye10032.utils.weibo.WeiboSetItem;
import com.aye10032.utils.weibo.WeiboUtils;
import okhttp3.OkHttpClient;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.aye10032.utils.timeutil.TimeUtils.MIN;
import static com.aye10032.utils.timeutil.TimeUtils.SEC;


/**
 * @author Dazo66
 */
public abstract class WeiboTask extends SubscribableBase {

    private Map<Long, Set<String>> postMap = new ConcurrentHashMap<>();
    private WeiboReader weiboReader;

    public WeiboTask(Zibenbot zibenbot, WeiboReader reader) {
        super(zibenbot);
        weiboReader = reader;
        File file = new File(getBot().appDirectory + "\\weibo\\");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public Date getNextTime(Date date) {
        Date ret = new Date();
        ret.setTime(date.getTime() + 5L * MIN + 10L * SEC);
        return ret;
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        Long weiboId = null;
        Zibenbot.logInfoStatic("开始检查微博");
        if (args == null || args.length == 0) {
            return;
        }
        OkHttpClient client = Zibenbot.getOkHttpClient();
        WeiboSet posts = new WeiboSet();
        try {
            posts = WeiboUtils.getWeiboSet(client, weiboId);
        } catch (Throwable e) {
            Zibenbot.logErrorStatic("获取微博出错：" + ExceptionUtils.printStack(e));
        }
        if (postMap.get(weiboId) == null) {
            Set<String> set = new HashSet<>();
            posts.forEach(post -> set.add(post.getId()));
            postMap.put(weiboId, set);
        } else {
            Iterator<WeiboSetItem> postIterator = posts.iterator();
            Set<String> postIds = this.postMap.get(weiboId);
            while (postIterator.hasNext()) {
                WeiboSetItem post = postIterator.next();
                if (postIds.contains(post.getId())) {
                    postIterator.remove();
                } else {
                    postIds.add(post.getId());
                    if (!post.isPerma()) {
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
    }
}
