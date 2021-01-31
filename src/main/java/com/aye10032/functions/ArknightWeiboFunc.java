package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.timetask.ArknightWeiboTask;
import com.aye10032.utils.weibo.WeiboSet;
import com.aye10032.utils.weibo.WeiboSetItem;
import com.aye10032.utils.weibo.WeiboUtils;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;

import java.util.ArrayList;

/**
 * @author Dazo66
 */
public class ArknightWeiboFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;
    private WeiboSet posts = null;

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
                        builder.append("数据读取出错，可能是网络问题");
                    } else {
                        builder.append("最近的饼如下：").append("\n");
                        WeiboSetItem[] arrayPosts = posts.toArray(new WeiboSetItem[0]);
                        for (int i = 0; i < arrayPosts.length; i++) {
                            builder.append("\t").append("[").append(i + 1).append("]：");
                            if (arrayPosts[i].getIsTop()) {
                                builder.append("[置顶] ");
                            }
                            builder.append(arrayPosts[i].getTitle()).append("\n");
                        }
                        builder.append("输入[方舟公告]/[fzgg] + 序号进一步查看。");
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
                        replyMsg(s, getTask().postToUser(WeiboUtils.getWeiboWithPostItem(Zibenbot.getOkHttpClient(), arrayPosts[i]), s.isGroupMsg()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .ifNot(s -> replyMsg(s, "输入的数字有误，或者是不在[1-9]的范围内。"))
                .build();
    }

    private ArknightWeiboTask getTask() {
        return zibenbot.arknightWeiboTask;
    }

    private void setPosts() {
        posts = WeiboUtils.getWeiboSet(Zibenbot.getOkHttpClient(), 6279793937L);
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }
}
