package com.aye10032.util

import java.util.*

class MHWUtil {
    private val monsterList: MutableList<String> = ArrayList()
    private val monsterListIce: MutableList<String> = ArrayList()
    private val armsList: MutableList<String> = ArrayList()
    val aim: String
        get() {
            val random = Random()
            val m = random.nextInt(monsterList.size + monsterListIce.size)
            val n = random.nextInt(armsList.size)
            return if (m > monsterList.size) {
                "用" + armsList[n] + "打" + monsterListIce[m - monsterList.size]
            } else {
                "用" + armsList[n] + "打" + monsterList[m]
            }
        }

    val iceAim: String
        get() {
            val random = Random()
            val m = random.nextInt(monsterListIce.size)
            val n = random.nextInt(armsList.size)
            return "用" + armsList[n] + "打" + monsterListIce[m]
        }

    init {
        monsterList.add("蛮鄂")
        monsterList.add("丝瓜")
        monsterList.add("尸套")
        monsterList.add("烤鱼")
        monsterList.add("爆锤")
        monsterList.add("鹿首精")
        monsterList.add("惨爪")
        monsterList.add("麒麟")
        monsterList.add("钢龙")
        monsterList.add("骚鸟")
        monsterList.add("泥鱼龙")
        monsterList.add("冥灯龙")
        monsterList.add("熔山龙")
        monsterList.add("记者")
        monsterList.add("角龙")
        monsterList.add("黑角")
        monsterList.add("炎喵")
        monsterList.add("大凶鄂龙")
        monsterList.add("中分哥")
        monsterList.add("肥宅")
        monsterList.add("飞雷龙")
        monsterList.add("娜娜子")
        monsterList.add("咩咩子")
        monsterList.add("浮空龙")
        monsterList.add("阿爆")
        monsterList.add("毒妖鸟")
        monsterList.add("贝爷")
        monsterList.add("土砂龙")
        monsterList.add("渣渣辉")
        monsterList.add("骨锤龙")
        monsterList.add("雌火龙")
        monsterList.add("樱火龙")
        monsterList.add("雄火龙")
        monsterList.add("苍火龙")
        monsterList.add("风飘龙")
        monsterList.add("古代鹿首精")
        monsterListIce.add("冰鱼龙")
        monsterListIce.add("痹毒龙")
        monsterListIce.add("水妖鸟")
        monsterListIce.add("迅龙")
        monsterListIce.add("轰龙")
        monsterListIce.add("霜翼风漂龙")
        monsterListIce.add("硫斩龙")
        monsterListIce.add("冰咒龙")
        monsterListIce.add("雾瘴尸套龙")
        monsterListIce.add("惶怒恐暴龙")
        monsterListIce.add("天地煌啼龙")
        monsterListIce.add("黑狼鸟")
        monsterListIce.add("黑轰龙")
        monsterListIce.add("银火龙")
        monsterListIce.add("猛牛龙")
        monsterListIce.add("浮眠龙")
        monsterListIce.add("冰牙龙")
        monsterListIce.add("斩龙")
        monsterListIce.add("碎龙")
        monsterListIce.add("雷颚龙")
        monsterListIce.add("凶爪龙")
        monsterListIce.add("红莲爆鳞龙")
        monsterListIce.add("溟波龙")
        monsterListIce.add("歼世灭尽龙")
        monsterListIce.add("雷狼龙")
        monsterListIce.add("金火龙")
        monsterListIce.add("狱狼龙")
        monsterListIce.add("金狮子")
        monsterListIce.add("萌宝碎龙")
        monsterListIce.add("激昂金狮子")
        monsterListIce.add("冰原渣渣灰")
        monsterListIce.add("战痕黑狼鸟")
        monsterListIce.add("冥赤龙")
        armsList.add("狩猎笛")
        armsList.add("大锤")
        armsList.add("太刀")
        armsList.add("片手")
        armsList.add("双刀")
        armsList.add("大剑")
        armsList.add("轻弩")
        armsList.add("重弩")
        armsList.add("操虫棍")
        armsList.add("弓")
        armsList.add("铳枪")
        armsList.add("长枪")
        armsList.add("盾斧")
        armsList.add("斩斧")
    }
}
