package com.aye10032.utils.food;

public class FoodVariety {

    private String[] mainlist;
    private String[] ssrList;
    private String[] srList;

    private String[] canteen1;
    private String[] canteen1sr;
    private String[] canteen1ssr;

    public FoodVariety(){
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

        canteen1 = new String[]{"1.21","1.19","1.17","1.15","1.12","1.11","1.6","1.5","1.3",
                "2.21","2.20","2.18","2.4","2.8"};
        canteen1sr = new String[]{"1.7","2.14"};
        canteen1ssr = new String[]{"巨难吃烤冷面"};
    }

    public String[] getMainlist() {
        return mainlist;
    }

    public String[] getSrList() {
        return srList;
    }

    public String[] getSsrList() {
        return ssrList;
    }

    public String[] getCanteen1() {
        return canteen1;
    }

    public String[] getCanteen1sr() {
        return canteen1sr;
    }

    public String[] getCanteen1ssr() {
        return canteen1ssr;
    }
}
