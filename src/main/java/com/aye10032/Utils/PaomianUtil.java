package com.aye10032.Utils;

import java.util.Random;

public class PaomianUtil {

    private String[] Tongyi;
    private String[] Kangshifu;
    private String[] Tangdaren;
    private String[] Heweidao;

    public PaomianUtil() {
        Tongyi = new String[]{"红烧牛肉", "老坛酸菜", "香辣牛肉", "藤椒牛肉", "酸豆角排骨"};
        Kangshifu = new String[]{"香辣牛肉", "红烧牛肉", "老坛酸菜", "雪菜肉丝", "藤椒牛肉", "葱香排骨", "爆椒牛肉", "红烧排骨"
                , "铁板牛排", "鲜虾鱼板", "卤香牛肉"};
        Heweidao = new String[]{"咖喱牛肉", "意大利牛肉", "麻辣牛肉", "海鲜", "香辣海鲜", "虾仁", "香辣牛肉", "五香牛肉", "猪骨浓汤"};
        Tangdaren = new String[]{"海鲜拉面", "日式豚骨拉面", "罗宋汤面", "酸辣豚骨面"};
    }

    public String getType() {
        Random random = new Random();
        int m = random.nextInt(Tongyi.length + Kangshifu.length + Heweidao.length + Tangdaren.length);

        String msg = "";

        if (m < Tongyi.length) {
            msg = "统一" + Tongyi[m] + "面";
        } else if (m - Tongyi.length < Kangshifu.length) {
            m -= Tongyi.length;
            msg = "康师傅" + Kangshifu[m] + "面";
        } else if (m - (Tongyi.length + Kangshifu.length) < Tangdaren.length) {
            m -= Tongyi.length + Kangshifu.length;
            msg = "汤达人" + Tangdaren[m] + "";
        } else {
            m -= Tongyi.length + Kangshifu.length + Tangdaren.length;
            msg = "合味道" + Heweidao[m] + "风味";
        }

        return msg;
    }

    public String getType(int type){
        Random random = new Random();

        String msg = "";
        int n;

        switch (type){
            case 1:
                n = random.nextInt(Tongyi.length);
                msg = msg = "统一" + Tongyi[n] + "面";
                break;
            case 2:
                n = random.nextInt(Kangshifu.length);
                msg = "康师傅" + Kangshifu[n] + "面";
                break;
            case 3:
                n = random.nextInt(Tangdaren.length);
                msg = "汤达人" + Tangdaren[n] + "";
                break;
            case 4:
                n = random.nextInt(Heweidao.length);
                msg = "合味道" + Heweidao[n] + "风味";
                break;
        }


        return msg;
    }

}
