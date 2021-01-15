package com.aye10032.utils;

import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.util.*;
import java.util.function.Function;

public class StringUtil {

    /*public static void main(String[] args) {
        String msg = "搬运 link1 树场 其一";
        System.out.println(new StringUtil().split(msg)[2]);
    }*/

    /**
     * 分割MessageChain根据MAX_LENGTH
     *
     * @param chain      需要分割的
     * @param MAX_LENGTH 单条消息最长长度
     * @return 一个MessageChain列表
     */
    public static List<MessageChain> longMsgSplit(MessageChain chain, final int MAX_LENGTH) {
        List<Message> list = new ArrayList<>();

        for (Message message : chain) {
            if (message.toString().length() >= MAX_LENGTH) {
                if (message instanceof PlainText) {
                    String s = message.toString();
                    String[] strings = s.split("\n");
                    for (String s2 : strings) {
                        if (s2.length() > MAX_LENGTH) {
                            split(s2, MAX_LENGTH).forEach(s1 -> list.add(new PlainText(s1)));
                            list.add(new PlainText("\n"));
                        } else {
                            list.add(new PlainText(s2));
                            list.add(new PlainText("\n"));
                        }
                    }
                    list.remove(list.size() - 1);
                }
            } else {
                list.add(message);
            }
        }
        List<MessageChain> ret = new ArrayList<>();
        int i = 0;
        MessageChainBuilder builder = new MessageChainBuilder();
        for (Message message : list) {
            String s = message.toString();
            if (i + s.length() >= MAX_LENGTH) {
                ret.add(builder.build());
                builder = new MessageChainBuilder();
                builder.add(message);
                i = s.length();
            } else {
                builder.add(message);
                i += s.length();
            }
        }
        //把剩余的加进去
        if (builder.size() != 0) {
            ret.add(builder.build());
        }
        return ret;
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
