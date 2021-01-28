package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.timetask.ArknightWeiboTask;
import com.aye10032.utils.WeiboPost;
import com.aye10032.utils.WeiboUtils;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.concurrent.TimeUnit;

public class ArknightWeiboFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;
    private long last = 0;
    private LinkedHashSet<WeiboPost> posts = null;
    private final ArknightWeiboTask weiboTask = new ArknightWeiboTask(zibenbot) {
        @Override
        public String getName() {
            return null;
        }

        @Override
        public Date getNextTime(Date date) {
            return null;
        }
    };

    public ArknightWeiboFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        ArrayList<String> list = new ArrayList<>();
        list.add(".方舟公告");
        list.add("方舟公告");
        list.add("fzgg");
        list.add(".fzgg");
        commander = new CommanderBuilder<SimpleMsg>()
                .start()
                .or(list::contains)
                .run(s -> {
                    setPosts();
                    StringBuilder builder = new StringBuilder();
                    if (posts.isEmpty()) {
                        builder.append("数据读取出错， 可能是网络问题");
                    } else {
                        builder.append("最近的饼如下：").append("\n");
                        WeiboPost[] arrayPosts = posts.toArray(new WeiboPost[0]);
                        for (int i = 0; i < arrayPosts.length; i++) {
                            builder.append("\t").append("[").append(i + 1).append("]：").append(arrayPosts[i].getTitle()).append("\n");
                        }
                        builder.append("输入[方舟公告]/[fzgg] + 序号进一步查看。");
                        replyMsg(s, builder.toString());
                    }
                })
                .next()
                .or(s -> {
                    try {
                        int i = Integer.parseInt(s);
                        if (posts == null || posts.isEmpty() || System.currentTimeMillis() - last > 1000 * 60 * 10) {
                            setPosts();
                        }
                        if (i > 0 && i <= posts.size()) {
                            return true;
                        }
                    } catch (Exception e) {
                    }
                    return false;

                })
                .run(s -> {
                    int i = Integer.parseInt(s.getCommandPieces()[1]) - 1;
                    WeiboPost[] arrayPosts = posts.toArray(new WeiboPost[0]);
                    replyMsg(s, weiboTask.postToUser(arrayPosts[i]));
                })
                .build();
    }

    private void setPosts() {
        weiboTask.client = weiboTask.client.newBuilder().callTimeout(10, TimeUnit.SECONDS)
                .proxy(Zibenbot.getProxy()).build();
        posts = WeiboUtils.getRecent10Post(weiboTask.client,
                "https://rssfeed.today/weibo/rss/6279793937");
        last = System.currentTimeMillis();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }
}
