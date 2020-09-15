package com.aye10032.utils.banutil;

import java.util.*;

public class AyeGroup {

    private long Group;
    private Map<Long, AyeMember> memberMap = new HashMap<Long, AyeMember>();
    private List<Long> banList = new ArrayList<Long>();
    private LinkedHashMap<Long, Integer> banedMap = new LinkedHashMap<Long, Integer>();
    private List<Map.Entry<Long, Integer>> sortlist;
    private LinkedHashMap<Long, Integer> banOtherMap = new LinkedHashMap<Long, Integer>();

    public AyeGroup(long group) {
        this.Group = group;
    }

    private boolean MemebrExist(long QQ) {
        return memberMap.containsKey(QQ);
    }

    public void addBan(long QQ) {
        banList.add(QQ);
    }

    public List<Long> getBanList() {
        return this.banList;
    }

    public void clearBanList() {
        banList.clear();
    }

    public AyeMember getMemberObject(long QQ) {
        return memberMap.get(QQ);
    }

    public void addMemebrBanedTime(long fromQQ, long baned) {
        if (!MemebrExist(fromQQ)) {
            AyeMember member = new AyeMember(fromQQ);
            memberMap.put(fromQQ, member);
        }
        if (!MemebrExist(baned)) {
            AyeMember member = new AyeMember(baned);
            memberMap.put(baned, member);
        }
        getMemberObject(fromQQ).addBanOtherTime();
        getMemberObject(baned).addBanedTime();
    }

    public int getMemberBandeTime(long QQ) {
        if (!MemebrExist(QQ)) {
            return 0;
        } else {
            AyeMember member = memberMap.get(QQ);
            return member.getBanedTime();
        }
    }

    public int getMemberBanOtherTime(long QQ) {
        if (!MemebrExist(QQ)) {
            return 0;
        } else {
            AyeMember member = memberMap.get(QQ);
            return member.getBanOtherTime();
        }
    }

    private void sortBanedTime() {
        Iterator<Map.Entry<Long, AyeMember>> entries = memberMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Long, AyeMember> entry = entries.next();
            long QQ = entry.getKey();
            int times = entry.getValue().getBanedTime();
            banedMap.put(QQ, times);
            sortlist = new ArrayList<>(banedMap.entrySet());
            sortlist.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        }
    }

    public List<Map.Entry<Long, Integer>> getBanedTimeRank() {
        sortBanedTime();
        return this.sortlist;
    }

}
