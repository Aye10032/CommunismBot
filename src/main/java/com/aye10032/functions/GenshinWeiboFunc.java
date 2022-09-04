package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.timetask.ArknightWeiboTask;
import com.aye10032.utils.weibo.WeiboReader;
import com.aye10032.utils.weibo.WeiboSet;
import com.aye10032.utils.weibo.WeiboSetItem;
import com.aye10032.utils.weibo.WeiboUtils;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author Dazo66
 */
@Service
public class GenshinWeiboFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;
    private WeiboSet posts = null;
    private WeiboReader reader;

    public GenshinWeiboFunc(Zibenbot zibenbot, WeiboReader reader) {
        super(zibenbot);
        this.reader = reader;
    }

    @Override
    public void setUp() {
        ArrayList<String> list = new ArrayList<>();
        list.add(".原神公告");
        list.add("原神公告");
        list.add("ysgg");
        list.add(".ysgg");
        commander = new CommanderBuilder<SimpleMsg>()
                .start()
                .or(list::contains)
                .run(s -> {
                    setPosts();
                    StringBuilder builder = new StringBuilder();
                    if (posts.isEmpty()) {
                        builder.append("数据读取出错，可能是网络问题");
                    } else {
                        builder.append("原神最近的饼如下：").append("\n");
                        WeiboSetItem[] arrayPosts = posts.toArray(new WeiboSetItem[0]);
                        for (int i = 0; i < arrayPosts.length; i++) {
                            builder.append("\t").append("[").append(i + 1).append("]：");
                            if (arrayPosts[i].getIsTop()) {
                                builder.append("[置顶] ");
                            }
                            builder.append(arrayPosts[i].getTitle()).append("\n");
                        }
                        builder.append("输入[原神公告]/[ysgg] + 序号进一步查看。");
                        replyMsg(s, builder.toString());
                    }
                })
                .next()
                .or(s -> {
                    try {
                        int i = Integer.parseInt(s);
                        setPosts();
                        if (i > 0 && i <= posts.size()) {
                            return true;
                        }
                    } catch (Exception e) {
                    }
                    return false;

                })
                .run(s -> {
                    int i = Integer.parseInt(s.getCommandPieces()[1]) - 1;
                    WeiboSetItem[] arrayPosts = posts.toArray(new WeiboSetItem[0]);
                    try {
                        if (arrayPosts[i].isOffAnnounce()) {
                            zibenbot.logInfo(String.format("检测到原神微博更新（来自官网）：%s", arrayPosts[i].getTitle()));
                            replyMsg(s, reader.postToUser(ArknightWeiboTask.getPostFromOff(arrayPosts[i])));
                        } else {
                            replyMsg(s, reader.postToUser(WeiboUtils.getWeiboWithPostItem(Zibenbot.getOkHttpClient(), arrayPosts[i])));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .ifNot(s -> replyMsg(s, "输入的数字有误，或者是不在[1-9]的范围内。"))
                .build();
    }

    private void setPosts() {
        posts = WeiboUtils.getWeiboSet(Zibenbot.getOkHttpClient(), 6593199887L);
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

}


