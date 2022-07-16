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


/**
 * @author Dazo66
 */
public abstract class WeiboTask extends SubscribableBase {

    private Map<Long, Set<String>> postMap = new ConcurrentHashMap<>();
    private WeiboReader weiboReader;

    public WeiboTask(Zibenbot zibenbot, WeiboReader reader) {
        super(zibenbot);
        weiboReader = reader;
        File file = new File(getBot().appDirectory + "/weibo/");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public Date getNextTime(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date(date.getTime()));
        int amount = instance.get(Calendar.MINUTE);
        instance.add(Calendar.MINUTE, 5 - amount % 5);
        return instance.getTime();
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
