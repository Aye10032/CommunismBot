package com.aye10032.foundation.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

public class StringUtil {

    /*public static void main(String[] args) {
        String msg = "搬运 link1 树场 其一";
        System.out.println(new StringUtil().split(msg)[2]);
    }*/

    public static String encryptToBase64(String filePath) {
        if (filePath == null) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List<String> split(String s, int length) {
        List<String> ret = new ArrayList<>();
        int size = s.length() / length + 1;
        for (int i = 0; i < size; i++) {
            int i1 = (i + 1) * length;
            if (i1 > s.length()) {
                i1 = s.length();
            }
            ret.add(s.substring(i * length, i1));
        }
        return ret;
    }

    /**
     * 对传进来的多对象进行拼接
     *
     * @param middleKey  用于拼接的中间字符串
     * @param collection 对象的集合
     * @return 拼接好的字符串
     */
    public static <T> String splicing(String middleKey, Collection<T> collection) {
        return splicing(middleKey, collection, T::toString);
    }

    /**
     * 对传进来的多对象进行拼接
     *
     * @param middleKey 用于拼接的中间字符串
     * @param array     对象的集合
     * @return 拼接好的字符串
     */
    public static <T> String splicing(String middleKey, T... array) {
        return splicing(middleKey, Arrays.asList(array), T::toString);
    }

    /**
     * 对传进来的多对象进行拼接
     *
     * @param middleKey     用于拼接的中间字符串
     * @param array         对象的集合
     * @param getStringFunc 对象提取字符串的方式
     * @return 拼接好的字符串
     */
    public static <T> String splicing(String middleKey, T[] array, Function<T, String> getStringFunc) {
        return splicing(middleKey, Arrays.asList(array), getStringFunc);
    }

    /**
     * 对传进来的多对象进行拼接
     *
     * @param middleKey     用于拼接的中间字符串
     * @param collection    对象的集合
     * @param getStringFunc 对象提取字符串的方式
     * @return 拼接好的字符串
     */
    public static <T> String splicing(String middleKey, Collection<T> collection, Function<T, String> getStringFunc) {
        StringBuilder builder = new StringBuilder();
        Iterator<T> iterator = collection.iterator();
        for (int i = 0; i < collection.size(); i++) {
            builder.append(getStringFunc.apply(iterator.next()));
            if (i != collection.size() - 1) {
                builder.append(middleKey);
            }
        }
        return builder.toString();
    }

    public String[] split(String string) {
        String[] srcStr = string.split(" ");
        String[] dstStr = null;

        if (srcStr.length <= 3) {
            dstStr = srcStr;
        } else {
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
