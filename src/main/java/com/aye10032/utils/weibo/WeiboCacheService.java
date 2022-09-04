package com.aye10032.utils.weibo;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2022/9/4 13:31
 **/
public class WeiboCacheService {

    private static Map<String, Set<String>> cache = new ConcurrentHashMap<>();


    public static Set<String> getCacheIds(Class clazz) {
        return cache.computeIfAbsent(clazz.getName(), key -> new CopyOnWriteArraySet<>());
    }

    public static Set<String> getCacheIds(Object o) {
        return cache.computeIfAbsent(o.toString(), key -> new CopyOnWriteArraySet<>());
    }
}
