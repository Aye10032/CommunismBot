package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ConfigLoader;
import com.aye10032.utils.ImgUtils;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.video.LiveInfo;
import javafx.util.Pair;

import java.io.File;
import java.util.Date;
import java.util.List;

import static com.aye10032.utils.timeutil.TimeUtils.MIN;

public abstract class LiveTask extends SubscribableBase {

    private Zibenbot zibenbot;

    public LiveTask(Zibenbot zibenbot) {
        super(zibenbot);
        this.zibenbot = zibenbot;
    }

    @Override
    public Date getNextTime(Date date) {
        Date ret = new Date();
        ret.setTime(date.getTime() + 5 * MIN);
        return ret;
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        StringBuilder msg_builder = new StringBuilder();
        Date now = new Date();
        if (recivers != null) {
            for (Reciver reciver : recivers) {
                LiveInfo liveInfo = new LiveInfo(args[0]);
                if (liveInfo.HasLive() && liveInfo.Is_living()) {
                    long l = now.getTime() - liveInfo.getLive_date().getTime();
                    long min = ((l / (60 * 1000)));
                    if (min < 5) {
                        ImgUtils.downloadImg(liveInfo.getLive_background_url(), args[0], zibenbot.appDirectory);

                        msg_builder.append(liveInfo.getNickName(liveInfo.getUid())).append("正在直播：")
                                .append(liveInfo.getLive_title())
                                .append(zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\" + args[0] + ".jpg")))
                                .append("\n").append(liveInfo.getLive_url());
                        zibenbot.replyMsg(reciver.getSender(), msg_builder.toString());
                    }
                }
            }
        }
    }

    @Override
    public Pair<Boolean, String> argsCheck(String[] args) {
        LiveInfo liveInfo = new LiveInfo(args[0]);
        if (liveInfo.HasLive()){
            return new Pair<>(true,"");
        }else {
            return new Pair<>(false,"直播间添加失败");
        }
    }
}
