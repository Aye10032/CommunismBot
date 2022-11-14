package com.dazo66.command;

import com.rometools.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CommanderUtils {

    /**
     * 多关键词匹配
     * @param keywords 关键词列表
     * @param keyword 指定关键词
     * @return 如果关键字列表中存在指定关键词 返回true 否则返回false
     */
    public static boolean multiMatch(List<String> keywords, String keyword) {
        if (Lists.isEmpty(keywords)) {
            return false;
        } else {
            return keywords.contains(keyword);
        }
    }


    /**
     * 多关键词匹配
     * @param keywords 关键词列表
     * @param keyword 指定关键词
     * @return 如果关键字列表中存在指定关键词 返回true 否则返回false
     */
    public static boolean multiMatchIgnoreCase(List<String> keywords, String keyword) {
        if (Lists.isEmpty(keywords) || StringUtils.isEmpty(keyword)) {
            return false;
        } else {
            for (String s : keywords) {
                if (keyword.equalsIgnoreCase(s)) {
                    return true;
                }
            }
            return true;
        }
    }


}
