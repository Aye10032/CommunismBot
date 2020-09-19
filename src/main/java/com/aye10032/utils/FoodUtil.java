package com.aye10032.utils;

import com.aye10032.utils.food.FoodVariety;

import java.util.Random;

public class FoodUtil {

    private final FoodVariety foodVariety;

    public FoodUtil() {
        foodVariety = new FoodVariety();
    }

    public String eatWhat() {
        Random random = new Random();
        int m = random.nextInt(foodVariety.getMainlist().length);

        return foodVariety.getMainlist()[m];
    }

    public String[] eatWhatWithSSR(int tag) {
        String[] ssrList,srList,mainlist;
        if (tag == 1){
            mainlist = foodVariety.getCanteen1();
            srList = foodVariety.getSrList();
            ssrList = foodVariety.getSrList();
        }else{
            mainlist = foodVariety.getMainlist();
            srList = foodVariety.getSrList();
            ssrList = foodVariety.getSsrList();
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
                m = random.nextInt(foodVariety.getSsrList().length);
                food[0] = "★★★ " + foodVariety.getSsrList()[m];
                food[1] = "3";
            } else {
                m = random.nextInt(foodVariety.getSrList().length);
                food[0] = "★★ " + foodVariety.getSrList()[m];
                food[1] = "2";
            }
        } else if (flag == 3) {
            m = random.nextInt(foodVariety.getSsrList().length);
            food[0] = "★★★ " + foodVariety.getSsrList()[m];
            food[1] = "3";
        }

        return food;
    }

}
