package com.aye10032.foundation.utils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author dazo
 */
public class DateUtils {


    public static String formatDate(Date date, String pattern) {
        return DateTimeFormatter.ofPattern(pattern)
                .withZone(ZoneId.systemDefault())
                .format(date.toInstant());


    }
}
