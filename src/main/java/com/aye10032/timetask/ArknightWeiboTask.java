package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ExceptionUtils;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.timeutil.TimeUtils;
import com.aye10032.utils.weibo.WeiboReader;
import com.aye10032.utils.weibo.WeiboSet;
import com.aye10032.utils.weibo.WeiboSetItem;
import com.aye10032.utils.weibo.WeiboUtils;
import okhttp3.OkHttpClient;

import java.io.File;
import java.util.*;


/**
 * @author Dazo66
 */
public abstract class ArknightWeiboTask extends SubscribableBase {

    private Set<String> postIds = new HashSet<>();
    private WeiboReader weiboReader;

    public ArknightWeiboTask(Zibenbot zibenbot, WeiboReader reader) {
        super(zibenbot);
        weiboReader = reader;
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
        c.set(Calendar.SECOND, 1);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour < 8 || hour > 22) {
            c.set(Calendar.HOUR_OF_DAY, 8);
            c.set(Calendar.MINUTE, 5);
            if (hour >= 8) {
                c.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        return c.getTime();
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        OkHttpClient client = Zibenbot.getOkHttpClient();
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
                            getBot().logInfo(String.format("检测到方舟新的饼：%s", post.getTitle()));
                            replyAll(recivers, weiboReader.postToUser(WeiboUtils.getWeiboWithPostItem(client, post)));
                        } catch (Exception e) {
                            getBot().logWarning("获取饼出错：" + ExceptionUtils.printStack(e));
                        }
                    }
                }
            }
        }
    }

}
