package com.aye10032.foundation.utils;

import com.zhipu.oapi.utils.StringUtils;

import java.util.*;

/**
 * CQ码解析
 *
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
        text = text.replace("&amp;", "&");
        text = text.replace("&#91;", "[");
        text = text.replace("&#93;", "]");
        return text;
    }

    private static String decodeValue(String text) {
        text = decodeText(text);
        text = text.replace("&#44;", ",");
        return text;
    }


    /**
     * 分割含有CQ码的消息
     *
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
            if (i1 == -1) {
                if (cqCodeMessage.length() -1 > i) {
                    list.add(cqCodeMessage.substring(i));
                }
                break;
            }

        }
        if (list.isEmpty()) {
            return Collections.singletonList(cqCodeMessage);
        }
        return list;
    }

    private static Map<String, String> parseElement(String element) {
        Map<String, String> map = new HashMap<>();
        final String raw = element.trim();
        if (element.startsWith("[CQ:")) {
            element = element.substring(0, element.length() - 1);
            String[] split = element.split(",");
            String type = split[0].substring(split[0].indexOf(":") + 1);
            map.put("CQ", type);
            for (int i = 1; i < split.length; i++) {
                int i1 = split[i].indexOf("=");
                map.put(split[i].substring(0, i1), decodeValue(split[i].substring(i1 + 1)));
                map.put("raw", raw);
            }
        } else {
            map.put("CQ", "text");
            String value = decodeText(raw);
            map.put("value", value);
            map.put("raw", raw);
        }
        if (map.get("CQ").equals("json")) {
            //map.put("data", map.get("data").replaceAll("\\\\", ""));
        }
        return map;
    }

    public static void main(String[] args) {
        String data = "your encoded data here[CQ:image,file=http://baidu.com/1.jpg,type=show,id=40004] [CQ:at,qq=10001000]";
        decodeTest(data);
        data = "[CQ:json,data={\"ver\":\"1.0.0.19\"&#44;\"prompt\":\"&#91;QQ小程序&#93;大伟哥泪洒原神FES舞台！正面回应\\\"原神制作组傲慢\\\"的说法&#44;大伟哥带领项目组到处跑去找各地的旅行者聊真正的想法！直言我们什么都可以变，只要旅行者开心就好！看哭了\"&#44;\"config\":{\"type\":\"normal\"&#44;\"width\":0&#44;\"height\":0&#44;\"forward\":1&#44;\"autoSize\":0&#44;\"ctime\":1723877560&#44;\"token\":\"34f37f723e4b8f6c33d79a9bbeb5a1b1\"}&#44;\"needShareCallBack\":false&#44;\"app\":\"com.tencent.miniapp_01\"&#44;\"view\":\"view_8C8E89B49BE609866298ADDFF2DBABA4\"&#44;\"meta\":{\"detail_1\":{\"appid\":\"1109937557\"&#44;\"appType\":0&#44;\"title\":\"哔哩哔哩\"&#44;\"desc\":\"大伟哥泪洒原神FES舞台！正面回应\\\"原神制作组傲慢\\\"的说法&#44;大伟哥带领项目组到处跑去找各地的旅行者聊真正的想法！直言我们什么都可以变，只要旅行者开心就好！看哭了\"&#44;\"icon\":\"https:\\/\\/open.gtimg.cn\\/open\\/app_icon\\/00\\/95\\/17\\/76\\/100951776_100_m.png?t=1723629628\"&#44;\"preview\":\"i0.hdslb.com\\/bfs\\/share_ttl\\/302ca5bd24206f64e1f4a3d63730292b8080b499.jpg\"&#44;\"url\":\"m.q.qq.com\\/a\\/s\\/abb32ee00b29eea4c1ce7d620e95b736\"&#44;\"scene\":1036&#44;\"host\":{\"uin\":939290931&#44;\"nick\":\"赤月\"}&#44;\"shareTemplateId\":\"8C8E89B49BE609866298ADDFF2DBABA4\"&#44;\"shareTemplateData\":{}&#44;\"qqdocurl\":\"https:\\/\\/b23.tv\\/WuA9dPx?share_medium=android&amp;share_source=qq&amp;bbid=XY9E8119F492C7CA5E31E0FE0098D37394261&amp;ts=1723877560121\"&#44;\"showLittleTail\":\"\"&#44;\"gamePoints\":\"\"&#44;\"gamePointsUrl\":\"\"}}}]";
        List<Map<String, String>> maps = decodeTest(data);
        String data1 = maps.get(0).get("data");
        System.out.println(JsonUtils.fromJson(data1, Map.class));
    }

    private static List<Map<String, String>> decodeTest(String string) {
        long l = System.currentTimeMillis();
        List<Map<String, String>> decode = decode(string);
        System.out.println(System.currentTimeMillis() - l);
        System.out.println(JsonUtils.toJson(decode));
        return decode;
    }
}
