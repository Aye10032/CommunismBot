package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.utils.RSSUtil;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;

import static com.aye10032.utils.timeutil.TimeUtils.MIN;

/**
 * @program: communismbot
 * @className: MoeTask
 * @Description: 番剧订阅
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/7/10 上午 11:20
 */
public abstract class MoeTask extends SubscribableBase {
    private Zibenbot zibenbot;

    public MoeTask(Zibenbot zibenbot) {
        super(zibenbot);
        this.zibenbot = zibenbot;
    }

    @Override
    public Date getNextTime(Date date) {
        Date ret = new Date();
        ret.setTime(date.getTime() + 30L * MIN);
        return ret;
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        StringBuilder msg_builder = new StringBuilder();
        Date now = new Date();
        now.setTime(new Date().getTime() - 30L * MIN);
        if (recivers != null) {
            for (Reciver reciver : recivers) {
                msg_builder.append("你订阅的《").append(args[1]).append("》有资源更新了：");
                List<String> result = RSSUtil.getRSSUpdate(args[0], now);
                if (!result.isEmpty()){
                    for (String entry:result){
                        msg_builder.append("\n----------------\n").append(entry);
                    }
                    zibenbot.replyMsg(reciver.getSender(), msg_builder.toString());
                }
            }
        }
    }

    @Override
    public Pair<Boolean, String> argsCheck(String[] args) {
        if (args.length != 2) {
            return new Pair<>(false, "缺少参数");
        } else {
            if (args[0].startsWith("https://bangumi.moe/rss/")) {
                return new Pair<>(true, "");
            } else {
                return new Pair<>(false, "无效的RSS链接");
            }
        }
    }
}
