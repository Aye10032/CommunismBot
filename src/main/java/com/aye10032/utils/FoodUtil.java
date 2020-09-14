package com.aye10032.utils;

import java.util.Random;

public class FoodUtil {

    private String[] mainlist;
    private String[] ssrList;
    private String[] srList;

    public FoodUtil() {
        mainlist = new String[]{
                "中原大刀凉皮 凉面 擀面皮 梅干菜肉饼 葱油饼 酸辣粉 酸辣面",
                "东北全新鸡肉卷饼",
                "台湾特香酥鸡柳 炸串小吃",
                "高记腊汁肉夹馍",
                "吴记粥铺绿豆汤椰奶西米露",
                "柳州螺蛳粉 麻酱捞面",
                "炒饭炒面河粉米线",
                "铁板香豆腐狼牙土豆酱香饼",
                "东北味儿烤冷面",
                "桥头排骨",
                "户部巷大面筋",
                "寿司虾滑",
                "小夫妻米线",
                "披萨时光",
                "私房菜",
                "四果汤",
                "千里香馄饨",
                "章鱼小丸子",
                "广东肠粉",
                "冰粉，烤肠",
                "哈尔滨烤冷面"};
        ssrList = new String[]{"老四川", "金城餐厅", "红马甲", "村夫烤鱼"};
        srList = new String[]{"沙县小吃", "黄焖鸡", "阿婆面馆"};
    }

    public String eatWhat() {
        Random random = new Random();
        int m = random.nextInt(mainlist.length);

        String food = mainlist[m];
        return food;
    }

    public String[] eatWhatWithSSR() {
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
                m = random.nextInt(ssrList.length);
                food[0] = "★★★ " + ssrList[m];
                food[1] = "3";
            } else {
                m = random.nextInt(srList.length);
                food[0] = "★★ " + srList[m];
                food[1] = "2";
            }
        } else if (flag == 3) {
            m = random.nextInt(ssrList.length);
            food[0] = "★★★ " + ssrList[m];
            food[1] = "3";
        }

        return food;
    }

}
