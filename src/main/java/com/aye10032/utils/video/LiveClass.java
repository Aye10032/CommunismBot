package com.aye10032.utils.video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveClass {

    private Map<Long, List<String>> liveMap;

    public LiveClass() {
        liveMap = new HashMap<>();
    }

    public void addLive(long group, String live) {
        List<String> list;
        if (liveMap.containsKey(group)) {
            list = liveMap.get(group);
        } else {
            list = new ArrayList<>();
        }
        list.add(live);

        liveMap.put(group, list);
    }

    public boolean deleteLive(long group, String live) {
        List<String> list;
        if (liveMap.containsKey(group)) {
            list = liveMap.get(group);
            if (list.contains(live)) {
                list.remove(live);
            } else {
                return false;
            }
        } else {
            return false;
        }

        liveMap.put(group, list);
        return true;
    }

    public List<String> getList(long group){
        List<String> list;
        if (liveMap.containsKey(group)) {
            list = liveMap.get(group);
        } else {
            list = new ArrayList<>();
        }

        return list;
    }

}
