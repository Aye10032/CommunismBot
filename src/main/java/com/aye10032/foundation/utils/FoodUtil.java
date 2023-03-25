package com.aye10032.foundation.utils;

import com.aye10032.foundation.entity.base.EatData;

import java.util.Random;

public class FoodUtil {

    private final EatData eatData;

    public FoodUtil() {
        eatData = new EatData();
    }

    public String eatWhat() {
        Random random = new Random();
        int m = random.nextInt(eatData.getMainlist().length);

        return eatData.getMainlist()[m];
    }

    public String[] eatWhatWithSSR(int tag) {
        String[] ssrList, srList, mainlist;
        if (tag == 1) {
            mainlist = eatData.getCanteen1();
            srList = eatData.getSrList();
            ssrList = eatData.getSrList();
        } else {
            mainlist = eatData.getMainlist();
            srList = eatData.getSrList();
            ssrList = eatData.getSsrList();
        }
        Random random = new Random();
        int flag = random.nextInt(30);
        String[] food = new String[2];
        int m;
        if (flag == 1) {
            int srint = random.nextInt(10);
            if (srint == 1) {
                m = random.nextInt(ssrList.length);
                food[0] = "★★★ " + ssrList[m];
                food[1] = "3";
            } else {
                m = random.nextInt(srList.length);
                food[0] = "★★ " + srList[m];
                food[1] = "2";
            }
        } else {
            m = random.nextInt(mainlist.length);
            food[0] = "★ " + mainlist[m];
            food[1] = "1";
        }

        return food;
    }

    public String[] eatGuaranteed(int flag) {
        String[] food = new String[2];
        Random random = new Random();
        int m;
        if (flag == 2) {
            int srint = random.nextInt(10);
            if (srint == 1) {
                m = random.nextInt(eatData.getSsrList().length);
                food[0] = "★★★ " + eatData.getSsrList()[m];
                food[1] = "3";
            } else {
                m = random.nextInt(eatData.getSrList().length);
                food[0] = "★★ " + eatData.getSrList()[m];
                food[1] = "2";
            }
        } else if (flag == 3) {
            m = random.nextInt(eatData.getSsrList().length);
            food[0] = "★★★ " + eatData.getSsrList()[m];
            food[1] = "3";
        }

        return food;
    }

}
