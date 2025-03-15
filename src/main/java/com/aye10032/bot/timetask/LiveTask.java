package com.aye10032.bot.timetask;

import com.aye10032.foundation.utils.ImgUtils;
import com.aye10032.foundation.utils.timeutil.Reciver;
import com.aye10032.foundation.utils.timeutil.SubscribableBase;
import com.aye10032.foundation.utils.video.LiveInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class LiveTask extends SubscribableBase {

    @Override
    public String getName() {
        return "直播公告小助手";
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        StringBuilder builder = new StringBuilder();
        Date now = new Date();
        if (recivers != null) {
            for (Reciver reciver : recivers) {
                LiveInfo liveInfo = new LiveInfo(args[0]);
                log.info("直播间" + args[0] + "检查结果：" + liveInfo.Is_living());
                if (liveInfo.HasLive() && liveInfo.Is_living()) {
                    long l = now.getTime() - liveInfo.getLiveDate().getTime();
                    long min = ((l / (60 * 1000)));
                    if (l < 5 * 60 * 1000 && l > 0) {
                        ImgUtils.downloadImg(liveInfo.getLiveBackgroundUrl(), args[0], getAppDirectory());

                        log.debug("尝试获取" + liveInfo.getUid() + "昵称");
                        builder.append(liveInfo.getNickName(liveInfo.getUid()))
                                .append("在").append(min).append("分钟前开始了直播：")
                                .append(liveInfo.getLiveTitle())
                                .append(getBot().getImg(new File(getAppDirectory() + "/image/" + args[0] + ".jpg")))
                                .append("\n").append(liveInfo.getLiveUrl());
                        getBot().replyMsg(reciver.getSender(), builder.toString());
                    }
                    log.debug("直播间" + args[0] + "直播开始于：" + min + "分钟之前");
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
