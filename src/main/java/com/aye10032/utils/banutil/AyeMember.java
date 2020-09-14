package com.aye10032.utils.banutil;

public class AyeMember {

    private long QQ;
    private int BanedTime = 0;
    private int BanOtherTime = 0;

    public AyeMember(long qq) {
        this.QQ = qq;
    }

    public int getBanedTime() {
        return BanedTime;
    }

    public void setBanedTime(int banedTime) {
        BanedTime = banedTime;
    }

    public void addBanedTime() {
        this.BanedTime++;
    }

    public int getBanOtherTime() {
        return BanOtherTime;
    }

    public void setBanOtherTime(int banOtherTime) {
        BanOtherTime = banOtherTime;
    }

    public void addBanOtherTime() {
        this.BanOtherTime++;
    }
}
