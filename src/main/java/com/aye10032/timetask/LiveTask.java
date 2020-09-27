package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ConfigLoader;
import com.aye10032.utils.ImgUtils;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.video.LiveClass;
import com.aye10032.utils.video.LiveInfo;

import java.io.File;
import java.util.Date;
import java.util.List;

import static com.aye10032.utils.timeutil.TimeUtils.MIN;

public abstract class LiveTask extends SubscribableBase {

    private Zibenbot zibenbot;
    private LiveClass liveClass;

    public LiveTask(Zibenbot zibenbot) {
        super(zibenbot);
        this.zibenbot = zibenbot;
        this.liveClass = ConfigLoader.load(zibenbot.appDirectory + "/liveData.json", LiveClass.class);
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
            liveClass = ConfigLoader.load(zibenbot.appDirectory + "/liveData.json", LiveClass.class);
            for (Reciver reciver : recivers) {
                List<String> live_list = liveClass.getList(reciver.getSender().getFromGroup());
                for (String live : live_list) {
                    LiveInfo liveInfo = new LiveInfo(live);
                    if (!liveInfo.HasLive()) {
                        long l = now.getTime() - liveInfo.getLive_date().getTime();
                        long min = ((l / (60 * 1000)));
                        if (min<5) {
                            ImgUtils.downloadImg(liveInfo.getLive_background_url(), live, zibenbot.appDirectory);

                            msg_builder.append(liveInfo.getNickName(liveInfo.getUid())).append("正在直播：")
                                    .append(liveInfo.getLive_title())
                                    .append(zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\" + live + ".jpg")))
                                    .append("\n").append(liveInfo.getLive_url());
                            zibenbot.replyMsg(reciver.getSender(), msg_builder.toString());
                        }
                    }
                }
            }
        }
    }
}
