package com.aye10032.foundation.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @program: communismbot
 * @className: FFXIVUtil
 * @Description: FF14func相关工具
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/8/16 下午 11:43
 */
public class FFXIVUtil {

    public static int daysBetween(Date last_date) {
        Calendar calendar = Calendar.getInstance();

        Calendar last_cal = Calendar.getInstance();
        last_cal.setTime(last_date);

        int day_last = last_cal.get(Calendar.DAY_OF_YEAR);
        int day_now = calendar.get(Calendar.DAY_OF_YEAR);

        int year_last = last_cal.get(Calendar.YEAR);
        int year_now = calendar.get(Calendar.YEAR);
        if (year_last != year_now) {
            int timeDistance = 0;
            for (int i = year_last; i < year_now; i++) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                if (gregorianCalendar.isLeapYear(i)) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day_now - day_last);
        } else {
            System.out.println("判断day2 - day1 : " + (day_now - day_last));
            return day_now - day_last;
        }
    }

}
