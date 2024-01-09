package com.aye10032.foundation.utils;

import java.util.Random;

/**
 * @author Aye10032
 */
public class RandomUtil {

    public static String getRandom(String[] list) {
        String result = "";
        int i = getRandomIndex(list.length);
        result = list[i];
        return result;
    }

    public static int getRandomIndex(int size) {
        Random random = new Random(System.currentTimeMillis());

        return random.nextInt(size);
    }

    public static boolean randomFlag(int percent) {
        Random random = new Random(System.currentTimeMillis());

        return random.nextInt(100) < percent;
    }

    /**
     * @param main_list       常规卡池数据
     * @param sr_list         稀有卡池数据
     * @param ssr_list        超稀有卡池数据
     * @param sr_probability  稀有卡概率（1/N）
     * @param ssr_probability 超稀有卡概率（1/N）
     */
    public static String[] getRandomWithSSR(String[] main_list, String[] sr_list, String[] ssr_list, int sr_probability, int ssr_probability) {
        Random random = new Random(System.currentTimeMillis());

        String[] result = new String[2];

        int total = sr_probability * ssr_probability;
        int flag = random.nextInt(total);

        if (flag < sr_probability) {
            result[0] = "★★★ " + getRandom(ssr_list);
            result[1] = "3";
        } else if (flag < ssr_probability) {
            result[0] = "★★ " + getRandom(sr_list);
            result[1] = "2";
        } else {
            result[0] = "★ " + getRandom(main_list);
            result[1] = "1";
        }

        return result;
    }


}
