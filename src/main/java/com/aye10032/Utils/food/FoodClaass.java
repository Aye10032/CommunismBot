package com.aye10032.Utils.food;

import java.util.ArrayList;
import java.util.List;

public class FoodClaass {

    private List<FoodData> dataList;

    public FoodClaass(){
        dataList = new ArrayList<FoodData>();
    }

    public void addOne(long fromQQ){
        boolean hasPerson = false;
        for (FoodData foodData:dataList){
            if (foodData.getFromQQ() == fromQQ){
                foodData.addTimes();
                hasPerson = true;
                break;
            }
        }
        if (!hasPerson){
            addPerson(fromQQ);
        }
    }

    public int getTimes(long fromQQ){
        int times = 0;
        boolean hasPerson = false;
        for (FoodData foodData:dataList){
            if (foodData.getFromQQ() == fromQQ){
                times = foodData.getTimes();
                hasPerson = true;
                break;
            }
        }
        if (!hasPerson){
            addPerson(fromQQ);
        }

        return times;
    }

    public void resetTimes(long fromQQ){
        for (FoodData foodData:dataList){
            if (foodData.getFromQQ() == fromQQ){
                foodData.resetTimes();
                break;
            }
        }
    }

    private void addPerson(long fromQQ){
        FoodData foodData = new FoodData(fromQQ);
        dataList.add(foodData);
    }

}
