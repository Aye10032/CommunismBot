package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.Config;
import com.aye10032.utils.ConfigLoader;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.video.LiveClass;
import com.aye10032.utils.video.LiveInfo;

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
        ret.setTime(date.getTime() + 5*MIN);
        return ret;
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        LiveInfo liveInfo = new LiveInfo("1478953");
        if (recivers != null){
            if (!liveInfo.HasLive()) {
                for (Reciver reciver : recivers) {
                    zibenbot.replyMsg(reciver.getSender(), "test"+args[1]);
                }
            }
        }
        ConfigLoader.save(zibenbot.appDirectory + "/liveData.json", LiveClass.class, liveClass);
    }
}
