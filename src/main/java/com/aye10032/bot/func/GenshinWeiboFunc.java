package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.aye10032.foundation.utils.weibo.WeiboReader;
import com.aye10032.foundation.utils.weibo.WeiboSet;
import com.aye10032.foundation.utils.weibo.WeiboSetItem;
import com.aye10032.foundation.utils.weibo.WeiboUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author Dazo66
 */
@Service
@Slf4j
public class GenshinWeiboFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;
    private WeiboSet posts = null;
    private WeiboReader reader;

    public GenshinWeiboFunc(BaseBot zibenbot, WeiboReader reader) {
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
                        replyMsg(s, reader.postToUser(WeiboUtils.getWeiboWithPostItem(Zibenbot.getOkHttpClient(), arrayPosts[i])));
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


