package com.aye10032.data;

public class EatData {

    private final String[] mainlist;
    private final String[] ssrList;
    private final String[] srList;

    private final String[] canteen1;
    private final String[] canteen1sr;
    private final String[] canteen1ssr;

    private final String[] Tongyi;
    private final String[] Kangshifu;
    private final String[] Tangdaren;
    private final String[] Heweidao;

    public EatData(){
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

        Tongyi = new String[]{"统一红烧牛肉", "统一老坛酸菜", "统一香辣牛肉", "统一藤椒牛肉", "统一酸豆角排骨"};
        Kangshifu = new String[]{"康师傅香辣牛肉", "康师傅红烧牛肉", "康师傅老坛酸菜", "康师傅雪菜肉丝", "康师傅藤椒牛肉",
                "康师傅葱香排骨", "康师傅爆椒牛肉", "康师傅红烧排骨", "康师傅铁板牛排", "康师傅鲜虾鱼板", "康师傅卤香牛肉"};
        Heweidao = new String[]{"合味道咖喱牛肉", "合味道意大利牛肉", "合味道麻辣牛肉", "合味道海鲜", "合味道香辣海鲜",
                "合味道虾仁", "合味道香辣牛肉", "合味道五香牛肉", "合味道猪骨浓汤"};
        Tangdaren = new String[]{"汤达人海鲜拉面", "汤达人日式豚骨拉面", "汤达人罗宋汤面", "汤达人酸辣豚骨面"};
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

    public String[] getTongyi() {
        return Tongyi;
    }

    public String[] getKangshifu() {
        return Kangshifu;
    }

    public String[] getHeweidao() {
        return Heweidao;
    }

    public String[] getTangdaren() {
        return Tangdaren;
    }
}
