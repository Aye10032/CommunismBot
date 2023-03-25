package com.aye10032.bot.timetask;

import com.aye10032.foundation.utils.timeutil.Reciver;
import com.aye10032.foundation.utils.timeutil.SubscribableBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * @Author Aye10032
 * @Date Created in 18:49 10.13
 */
@Service
@Slf4j
public class SleepTask extends SubscribableBase {

    @Override
    public String getName() {
        return "卞老师小助手";
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        if (recivers != null) {
            if (compareTo(7)) {
                for (Reciver reciver : recivers) {
                    getBot().replyMsg(reciver.getSender(),
                            getBot().getImg(new File(getBot().appDirectory + "/image/getup.jpg")));
                }
            } else if (compareTo(23)) {
                for (Reciver reciver : recivers) {
                    getBot().replyMsg(reciver.getSender(),
                            getBot().getImg(new File(getBot().appDirectory + "/image/sleep.jpg")));
                }
            }
        }
    }

    @Override
    public String getCron() {
        return "0 0 7,23 * * ? ";
    }

    private boolean compareTo(int aim_hour) {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        log.debug("now time -> " + hour + ":" + min);

        if (hour == aim_hour) {
            return true;
        } else if (hour + 1 == aim_hour) {
            return min >= 50;
        } else {
            return false;
        }
    }
}
