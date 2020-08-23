package com.aye10032.Utils.BanUtil;

import com.aye10032.Zibenbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BanRecord {

    private Map<Long, AyeGroup> groupMap = new HashMap<Long, AyeGroup>();
    private Zibenbot zibenbot;

    public BanRecord(Zibenbot zibenbot) {
        this.zibenbot = zibenbot;
    }

    private boolean GroupExist(long fromGroup) {
        return groupMap.containsKey(fromGroup);
    }

    public AyeGroup getGroupObject(long fromGroup) {
        if (!GroupExist(fromGroup)) {
            AyeGroup group = new AyeGroup(fromGroup);
            groupMap.put(fromGroup, group);
        }
        return groupMap.get(fromGroup);
    }

    public List<String> getKillRank(long fromGroup) {
        List<String> list = new ArrayList<String>();
        if (!GroupExist(fromGroup)) {
            list.add("本群往前的历史是一片空白，没有记载");
        } else {
            AyeGroup group = getGroupObject(fromGroup);
            List<Map.Entry<Long, Integer>> killedList = group.getBanedTimeRank();
            for (Map.Entry<Long, Integer> entry : killedList) {
                list.add(zibenbot.at(entry.getKey()) + "，被禁言" + entry.getValue() + "次\n");
            }
        }
        return list;
    }

}
