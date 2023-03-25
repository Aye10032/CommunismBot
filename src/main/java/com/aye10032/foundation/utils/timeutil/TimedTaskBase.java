package com.aye10032.foundation.utils.timeutil;

import java.util.Date;

/**
 * 时间任务的子类
 *
 * @author Dazo66
 */
public abstract class TimedTaskBase {

    private int times = -1;

    private long begin = -1;

    private long tiggerTime = System.currentTimeMillis();

    private ITimeAdapter cycle = TimeUtils.NEXT_DAY;

    public TimedTaskBase() {
    }

    public abstract void run(Date date);

    public int getTimes() {
        return times;
    }

    public ITimeAdapter getCycle() {
        return cycle;
    }

    public TimedTaskBase setCycle(ITimeAdapter cycle) {
        this.cycle = cycle;
        return this;
    }

    public Date getTiggerTime() {
        return new Date(tiggerTime);
    }

    public TimedTaskBase setTiggerTime(Date tiggerTime) {
        if (begin == -1) {
            this.begin = tiggerTime.getTime();
        }
        this.tiggerTime = tiggerTime.getTime();
        return this;
    }

    public Date getBegin() {
        return new Date(begin);
    }

    public Date getNextTiggerTime() {
        return TimeUtils.getNextTimeFromNowInclude(getTiggerTime(), getCycle());
    }

    public TimedTaskBase setTimes(int times) {
        this.times = times;
        return this;
    }
}
