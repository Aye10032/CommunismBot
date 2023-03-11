package com.aye10032.utils;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 16:24
 **/
public class RandomUtils {

    public static String getRandNumberString() {
        int min = 100;
        int max = 999;
        StringBuilder sb = new StringBuilder();
        sb.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        sb.append(org.apache.commons.lang3.RandomUtils.nextInt(min,max));
        return sb.toString();
    }

}
