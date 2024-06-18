package com.aye10032.foundation.utils;

import com.zhipu.oapi.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CQ码解析
 * @author dazo
 */
public class CQDecoder {

    public static List<Map<String, String>> decode(String data) {
        List<Map<String, String>> output = new ArrayList<>();
        List<String> list = splitMessage(data);
        for (String element : list) {
            output.add(parseElement(element));
        }
        return output;
    }

    private static String decodeText(String text) {
        text = text.replace("&", "&");
        text = text.replace("[", "[");
        text = text.replace("]", "]");
        return text;
    }

    private static String decodeValue(String text) {
        text = decodeText(text);
        text = text.replace("&#44", ",");
        return text;
    }


    /**
     * 分割含有CQ码的消息
     * @param cqCodeMessage
     * @return
     */
    private static List<String> splitMessage(String cqCodeMessage) {
        List<String> list = new ArrayList<>();
        int i = 0;
        while (true) {
            int i1 = cqCodeMessage.indexOf("[", i);
            if (i1 != -1) {
                int i2 = cqCodeMessage.indexOf("]", i1);
                if (i2 != -1) {
                    String left = cqCodeMessage.substring(i, i1);
                    if (!StringUtils.isEmpty(left)) {
                        list.add(left);
                    }
                    list.add(cqCodeMessage.substring(i1, i2 + 1));
                    i = i2 + 1;
                } else {
                    list.add(cqCodeMessage.substring(i));
                    return list;
                }
            }
            if (i1 == -1) break;

        }
        return list;
    }

    private static Map<String, String> parseElement(String element) {
        Map<String, String> map = new HashMap<>();
        if (element.startsWith("[CQ:")) {
            element = element.substring(0, element.length() - 1);
            String[] split = element.split(",");
            String type = split[0].substring(split[0].indexOf(":") + 1);
            map.put("CQ", type);
            for (int i = 1; i < split.length; i++) {
                int i1 = split[i].indexOf("=");
                map.put(split[i].substring(0, i1), decodeValue(split[i].substring(i1 + 1)));
                map.put("raw", element);
            }
        } else {
            map.put("CQ", "TEXT");
            map.put("value", decodeText(element));
            map.put("raw", element);
        }
        return map;
    }

    public static void main(String[] args) {
        String data = "your encoded data here[CQ:image,file=http://baidu.com/1.jpg,type=show,id=40004] [CQ:at,qq=10001000]";
        long l = System.currentTimeMillis();
        List<Map<String, String>> decode = decode(data);
        System.out.println(System.currentTimeMillis() - l);
        System.out.println(JsonUtils.toJson(decode));
    }
}
