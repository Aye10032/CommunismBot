package com.aye10032.Utils;

public class StringUtil {

    /*public static void main(String[] args) {
        String msg = "搬运 link1 树场 其一";
        System.out.println(new StringUtil().split(msg)[2]);
    }*/

    public StringUtil(){

    }

    public String[] split(String string){
        String[] srcStr = string.split(" ");
        String[] dstStr = null;

        if (srcStr.length <= 3){
            dstStr = srcStr;
        }else {
            dstStr = new String[3];
            dstStr[0] = srcStr[0];
            dstStr[1] = srcStr[1];

            StringBuilder desc = new StringBuilder();
            for (int i = 2; i < srcStr.length; i++) {
                desc.append(" ").append(srcStr[i]);
            }
            dstStr[2] = desc.toString().substring(1);
        }

        return dstStr;
    }

}
