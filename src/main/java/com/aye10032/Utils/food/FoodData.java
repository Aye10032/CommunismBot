package com.aye10032.Utils.food;

public class FoodData {

    private long FromQQ;
    private int Times;

    public FoodData(){

    }

    public FoodData(long fromQQ){
        this.FromQQ = fromQQ;
        this.Times = 0;
    }

    public long getFromQQ() {
        return FromQQ;
    }

    public int getTimes() {
        return Times;
    }

    public void addTimes() {
        Times++;
    }

    public void resetTimes(){
        Times = 0;
    }
}
