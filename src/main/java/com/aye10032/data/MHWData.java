package com.aye10032.data;

public class MHWData {

    private final String[] monster;
    private final String[] monster_ice;
    private final String[] arm;

    public MHWData(){
        monster = new String[]{"蛮鄂","丝瓜","尸套","烤鱼","爆锤","鹿首精","惨爪","麒麟","钢龙","骚鸟","泥鱼龙","冥灯龙",
                "熔山龙","记者","角龙","黑角","炎喵","大凶鄂龙","中分哥","肥宅","飞雷龙","娜娜子","咩咩子","浮空龙","阿爆",
                "毒妖鸟","贝爷","土砂龙","渣渣辉","骨锤龙","雌火龙","樱火龙","雄火龙","苍火龙","风飘龙","古代鹿首精"};
        monster_ice = new String[]{"冰鱼龙","痹毒龙","水妖鸟","迅龙","轰龙","霜翼风漂龙","硫斩龙","冰咒龙","雾瘴尸套龙",
                "惶怒恐暴龙","天地煌啼龙","黑狼鸟","黑轰龙","银火龙","猛牛龙","浮眠龙","冰牙龙","斩龙","碎龙","雷颚龙",
                "凶爪龙","红莲爆鳞龙","溟波龙","歼世灭尽龙","雷狼龙","金火龙","狱狼龙","金狮子","萌宝碎龙","激昂金狮子",
                "冰原渣渣灰","战痕黑狼鸟","冥赤龙"};
        arm = new String[]{"狩猎笛","大锤","太刀","片手","双刀","大剑","轻弩","重弩","操虫棍","弓","铳枪","长枪","盾斧","斩斧"};
    }

    public String[] getArm() {
        return arm;
    }

    public String[] getMonster() {
        return monster;
    }

    public String[] getMonster_ice() {
        return monster_ice;
    }
}
