package com.aye10032.timetask;

import com.aye10032.utils.ImgUtils;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.video.LiveInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class LiveTask extends SubscribableBase {

    @Override
    public String getName() {
        return "直播公告小助手";
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        StringBuilder msg_builder = new StringBuilder();
        Date now = new Date();
        if (recivers != null) {
            for (Reciver reciver : recivers) {
                LiveInfo liveInfo = new LiveInfo(args[0]);
                getBot().logDebug("直播间"+args[0]+"检查结果："+liveInfo.Is_living());
                if (liveInfo.HasLive() && liveInfo.Is_living()) {
                    long l = now.getTime() - liveInfo.getLive_date().getTime();
                    long min = ((l / (60 * 1000)));
                    if (min < 5) {
                        ImgUtils.downloadImg(liveInfo.getLive_background_url(), args[0], getBot().appDirectory);

                        msg_builder.append(liveInfo.getNickName(liveInfo.getUid()))
                            .append("在").append(min).append("分钟前开始了直播：")
                            .append(liveInfo.getLive_title())
                            .append(getBot().getImg(new File(getBot().appDirectory + "/image/" + args[0] + ".jpg")))
                            .append("\n").append(liveInfo.getLive_url());
                        getBot().replyMsg(reciver.getSender(), msg_builder.toString());
                    }
                }
            }
        }
    }

    @Override
    public String getCron() {
        return "0 0/5 * * * ? ";
    }

    @Override
    public Pair<Boolean, String> argsCheck(String[] args) {
        if (args.length == 0) {
            return Pair.of(false, "缺少参数");
        } else {
            LiveInfo liveInfo = new LiveInfo(args[0]);
            if (liveInfo.HasLive()) {
                return Pair.of(true, "");
            } else {
                return Pair.of(false, "直播间添加失败");
            }
        }
    }
}
