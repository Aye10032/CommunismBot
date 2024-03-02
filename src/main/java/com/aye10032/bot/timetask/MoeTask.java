package com.aye10032.bot.timetask;

import com.aye10032.foundation.utils.RSSUtil;
import com.aye10032.foundation.utils.timeutil.Reciver;
import com.aye10032.foundation.utils.timeutil.SubscribableBase;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.aye10032.foundation.utils.timeutil.TimeUtils.MIN;

/**
 * @program: communismbot
 * @className: MoeTask
 * @Description: 番剧订阅
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/7/10 上午 11:20
 */
@Service
public class MoeTask extends SubscribableBase {

    @Override
    public String getName() {
        return "番剧订阅小助手";
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        StringBuilder msg_builder = new StringBuilder();
        Date now = new Date();
        now.setTime(System.currentTimeMillis() - 30L * MIN);
        if (recivers != null) {
            for (Reciver reciver : recivers) {
                msg_builder.append("你订阅的《").append(args[1].replace("-", " ")).append("》有资源更新了：");
                List<String> result;
                if (args[0].startsWith("https://www.dmhy.org/topics/rss/")) {
                    result = RSSUtil.getAnimeUpdate(args[0], now, true);
                } else {
                    result = RSSUtil.getAnimeUpdate(args[0], now, false);
                }
                if (!result.isEmpty()) {
                    for (String entry : result) {
                        msg_builder.append("\n----------------\n").append(entry);
                    }
                    getBot().replyMsg(reciver.getSender(), msg_builder.toString());
                }
            }
        }
    }

    @Override
    public String getCron() {
        return "0 0/30 * * * ? ";
    }

    @Override
    public Pair<Boolean, String> argsCheck(String[] args) {
        if (args.length != 2) {
            return Pair.of(false, "参数错误，格式为RSS链接+番剧名称，并用-代替空格");
        } else {
            if (args[0].startsWith("https://bangumi.moe/rss/") || args[0].startsWith("https://www.dmhy.org/topics/rss/")) {
                return Pair.of(true, "");
            } else {
                return Pair.of(false, "无效的RSS链接");
            }
        }
    }
}
