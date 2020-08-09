package com.aye10032.Utils.TimeUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Dazo66
 */
public class TimeConstant {

    public static int SEC = 1000;
    public static int MIN = 60 * SEC;
    public static int HOUR = 60 * MIN;
    public static int DAY = 24 * HOUR;
    public static int WEEK = 7 * DAY;
    public static int YEAR = 365 * DAY;

    public static ITimeAdapter NEXT_YEAR = new NextYear();
    public static ITimeAdapter NEXT_MONTH = new NextMonth();
    public static ITimeAdapter NEXT_WEEK = new NextWeek();
    public static ITimeAdapter NEXT_DAY = new NextDay();
    public static ITimeAdapter NEXT_HOUR = new NextHour();
    public static ITimeAdapter NEXT_HALF_HOUR = new NextHalfHour();
    public static ITimeAdapter NEXT_MIN = new NextMin();
    public static ITimeAdapter NEXT_SEC = new NextSec();


    /**
     *
     * 得到下一个指定的日子，后面的数值表示指定的日期
     * 即：得下一个X月X日X小时X分X秒
     * -1表示不做要求
     *                                                                 month  day  hour min sec
     * Example:      getNextSpecialTime(Mon Jul 27 21:23:56 CST 2020  ,7,     1,   20,  50, 0);
     * return: 下一个7月1日20小时50分0秒  Thu Jul 01 20:50:00 CST 2021
     *
     * Example:      getNextSpecialTime(Mon Jul 27 21:23:56 CST 2020  ,7,    -1,   20,  50, 0);
     * return: 下一个7月X日20小时50分0秒  Tue Jul 27 20:50:00 CST 2021
     *
     * @param from 基准日期
     * @param month 指定的月份 [1-12]
     * @param day 指定的日期   [1-31]
     * @param hour 指定的小时  [0-24]
     * @param min 指定的分钟   [0-60]
     * @param sec 指定的秒数   [0-60]
     * @return 接下来的日期
     */
    public static Date getNextSpecialTime(Date from, int month, int day, int hour, int min,
                                          int sec) {
        int l1 = -1;
        month = month == -1 ? -1 : month - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);
        if (sec != -1) {
            calendar.set(Calendar.SECOND, sec);
            l1 = Calendar.MINUTE;
        }
        if (min != -1) {
            calendar.set(Calendar.MINUTE, min);
            l1 = Calendar.HOUR_OF_DAY;
        }
        if (hour != -1) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            l1 = Calendar.DAY_OF_MONTH;
        }
        if (day != -1) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            l1 = Calendar.MONTH;
        }
        if (month != -1) {
            calendar.set(Calendar.MONTH, month);
            l1 = Calendar.YEAR;
        }
        if (l1 == -1) {
            return from;
        } else {
            while (calendar.getTimeInMillis() <= from.getTime()) {
                calendar.add(l1, 1);
            }
        }
        return calendar.getTime();

    }

    /**
     * 当前时间的下一个
     * 不断运行cycel的循环直到找到现在的下一个
     *
     * @param date    基准时间
     * @param adapter 时间适配器 得到下个时间
     * @return
     */
    public static Date getNextTimeFromNow(Date date, ITimeAdapter adapter) {
        Date now = new Date();
        Date ret = (Date) date.clone();
        while (now.compareTo(ret) >= 0) {
            ret.setTime(adapter.getNextTime(ret).getTime());
        }
        return ret;
    }

    private static class NextYear implements ITimeAdapter {

        @Override
        public Date getNextTime(Date date) {
            Date ret = new Date();
            ret.setTime(date.getTime() + YEAR);
            return ret;
        }

    }

    private static class NextMonth implements ITimeAdapter {

        @Override
        public Date getNextTime(Date date) {
            Calendar ret = Calendar.getInstance();
            ret.setTime(date);
            ret.add(Calendar.MONTH, 1);
            return ret.getTime();
        }

    }

    private static class NextWeek implements ITimeAdapter {

        @Override
        public Date getNextTime(Date date) {
            Date ret = new Date();
            ret.setTime(date.getTime() + WEEK);
            return ret;
        }

    }

    private static class NextDay implements ITimeAdapter {

        @Override
        public Date getNextTime(Date date) {
            Date ret = new Date();
            ret.setTime(date.getTime() + DAY);
            return ret;
        }

    }

    private static class NextHalfHour implements ITimeAdapter {

        @Override
        public Date getNextTime(Date date) {
            Date ret = new Date();
            ret.setTime(date.getTime() + HOUR / 2);
            return ret;
        }

    }

    private static class NextHour implements ITimeAdapter {

        @Override
        public Date getNextTime(Date date) {
            Date ret = new Date();
            ret.setTime(date.getTime() + HOUR);
            return ret;
        }

    }

    private static class NextMin implements ITimeAdapter {

        @Override
        public Date getNextTime(Date date) {
            Date ret = new Date();
            ret.setTime(date.getTime() + MIN);
            return ret;
        }

    }

    private static class NextSec implements ITimeAdapter {

        @Override
        public Date getNextTime(Date date) {
            Date ret = new Date();
            ret.setTime(date.getTime() + SEC);
            return ret;
        }

    }


}
