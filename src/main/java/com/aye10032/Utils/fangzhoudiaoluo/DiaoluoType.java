package com.aye10032.Utils.fangzhoudiaoluo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dazo66
 */
public class DiaoluoType {

    public Material[] material;

    public Material getMaterialFromID(String id) {
        if (material != null) {
            for (Material material : this.material) {
                if (material.id.equals(id)) {
                    return material;
                }
            }
        }
        return null;
    }

    public static class Material {
        public String id = "";
        public int tier;
        public String name = "";
        public String credit_store_value = "";
        public String green_ticket_value = "";
        public String golden_ticket_value= "";
        public String type = "";
        public Stage[] lowest_ap_stages = new Stage[0];
        public Stage[] balanced_stages = lowest_ap_stages;
        public Stage[] drop_rate_first_stages = lowest_ap_stages;

        public Material(){}

    }

    public class Stage {
        public Drop[] extra_drop;
        public String code;
        public float drop_rate;
        public float efficiency;
        public float ap_per_item;
    }

    public class Drop {
        public String name;
        public int id;
    }

    public static class HeChenType {
        public String id;
        public String[] names;
        public String[] calls;

        public HeChenType(String id, String[] names, String[] calls) {
            this.id = id;
            List<String> list = new ArrayList<>();
            for (String c : calls) {
                list.add(c.trim());
            }
            this.calls = list.toArray(new String[]{});
            list = new ArrayList<>();
            for (String c : names) {
                list.add(c.trim());
            }
            this.names = list.toArray(new String[]{});
        }

        public boolean isThis(String name) {
            if (name.startsWith("*")) {
                name = name.substring(1);
            }
            for (String s : names) {
                if (s.trim().equals(name)) {
                    return true;
                }
            }
            return false;
        }

        public float maxSimilarity(String name) {
            float f = 0f;
            for (String s : names) {
                float f1 = levenshtein(name, s.trim());
                f = f1 > f ? f1 : f;
            }
            return f;
        }

        public float levenshtein(String str1,String str2) {
            //计算两个字符串的长度。
            int len1 = str1.length();
            int len2 = str2.length();
            //建立上面说的数组，比字符长度大一个空间
            int[][] dif = new int[len1 + 1][len2 + 1];
            //赋初值，步骤B。
            for (int a = 0; a <= len1; a++) {
                dif[a][0] = a;
            }
            for (int a = 0; a <= len2; a++) {
                dif[0][a] = a;
            }
            //计算两个字符是否一样，计算左上的值
            int temp;
            for (int i = 1; i <= len1; i++) {
                for (int j = 1; j <= len2; j++) {
                    if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                        temp = 0;
                    } else {
                        temp = 1;
                    }
                    //取三个值中最小的
                    dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1, dif[i - 1][j] + 1);
                }
            }

            return 1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
        }

        //得到最小值
        private static int min(int... is) {
            int min = Integer.MAX_VALUE;
            for (int i : is) {
                if (min > i) {
                    min = i;
                }
            }
            return min;
        }
    }

}
