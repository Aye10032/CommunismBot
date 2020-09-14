package com.aye10032.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dazo66
 */
public class Config {

    public Map<String, String> map;

    public String getWithDafault(String key, String defualt) {
        if (map != null) {
            if (map.containsKey(key)) {
                return map.get(key);
            } else {
                map.put(key, defualt);
                return defualt;
            }
        } else {
            set(key, defualt);
            return defualt;
        }
    }

    public String get(String key) {
        return map == null ? null : map.get(key);
    }

    public void set(String key, String value) {
        if (map == null) {
            map = Collections.synchronizedMap(new HashMap<>());
        }
        map.put(key, value);
    }


}
