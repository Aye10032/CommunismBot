package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.timeutil.TimeUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author Aye10032
 * @Date Created in 18:49 10.13
 */
public abstract class SleepTask extends SubscribableBase {

    private Zibenbot zibenbot;

    public SleepTask(Zibenbot zibenbot) {
        super(zibenbot);
        this.zibenbot = zibenbot;
    }

    @Override
    public Date getNextTime(Date date) {
        Date date1 = TimeUtils.getNextSpecialTime(date, -1, -1, 7, 0, 0, 0);
        Date date2 = TimeUtils.getNextSpecialTime(date, -1, -1, 23, 0, 0, 0);
        return TimeUtils.getMin(date1, date2);
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        if (recivers != null) {
            if (compareTo(6)) {
                for (Reciver reciver : recivers) {
                    zibenbot.replyMsg(reciver.getSender(),
                            zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\getup.jpg")));
                }
            } else if (compareTo(23)) {
                for (Reciver reciver : recivers) {
                    zibenbot.replyMsg(reciver.getSender(),
                            zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\sleep.jpg")));
                }
            }
        }
    }

    private boolean compareTo(int aim_hour) {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        if (hour == aim_hour) {
            return true;
        } else if (hour + 1 == aim_hour) {
            return min >= 50;
        } else {
            return false;
        }
    }
}
