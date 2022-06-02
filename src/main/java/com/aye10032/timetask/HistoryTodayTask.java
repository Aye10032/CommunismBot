package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.timeutil.TimeUtils;

import java.util.Date;
import java.util.List;

/**
 * @program: communismbot
 * @className: HistoryTodayTask
 * @Description: 历史上的今天订阅器
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/6/2 下午 6:15
 */
public abstract class HistoryTodayTask extends SubscribableBase {
    private Zibenbot zibenbot;

    public HistoryTodayTask(Zibenbot zibenbot){
        super(zibenbot);
        this.zibenbot = zibenbot;
    }

    @Override
    public Date getNextTime(Date date) {
        Date date1 = TimeUtils.getNextSpecialTime(date, -1, -1, 0, 0, 0, 0);
        return TimeUtils.getMin(date1);
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {

    }
}
