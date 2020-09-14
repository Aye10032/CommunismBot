package com.aye10032.functions;

import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.ConfigLoader;
import com.aye10032.utils.FoodUtil;
import com.aye10032.utils.food.FoodClaass;
import com.aye10032.Zibenbot;

/**
 * @author Dazo66
 */
public class EatFunc extends BaseFunc {

    FoodUtil foodUtil;
    FoodClaass foodClaass = ConfigLoader.load(zibenbot.appDirectory + "/foodData.json", FoodClaass.class);

    public EatFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        this.foodUtil = new FoodUtil();
    }

    public void run(SimpleMsg simpleMsg) {
        if (simpleMsg.getMsg().equals("晚饭")) {
            if (simpleMsg.getFromGroup() == 792666782L) {
                if (foodClaass.getTimes(simpleMsg.getFromClient()) == 99) {
                    foodClaass.resetTimes(simpleMsg.getFromClient());
                    String[] food = foodUtil.eatGuaranteed(3);
                    zibenbot.replyMsg(simpleMsg, food[0]);
                } else {
                    String[] food = foodUtil.eatWhatWithSSR();
                    zibenbot.replyMsg(simpleMsg, food[0]);
                    if (food[1].equals("3")) {
                        foodClaass.resetTimes(simpleMsg.getFromClient());
                    } else {
                        foodClaass.addOne(simpleMsg.getFromClient());
                    }
                }
            } else {
                String food = foodUtil.eatWhat();
                zibenbot.replyMsg(simpleMsg, food);
            }
            ConfigLoader.save(zibenbot.appDirectory + "/foodData.json", FoodClaass.class, foodClaass);
        } else if (simpleMsg.getFromGroup() == 792666782L && simpleMsg.getMsg().equals("晚饭十连")) {
            StringBuilder foodBuilder = new StringBuilder();
            boolean hasSSR = false;
            for (int i = 0; i < 9; i++) {
                if (foodClaass.getTimes(simpleMsg.getFromClient()) == 99) {
                    foodClaass.resetTimes(simpleMsg.getFromClient());
                    String[] food = foodUtil.eatGuaranteed(3);
                    zibenbot.replyMsg(simpleMsg, food[0]);
                } else {
                    String[] food = foodUtil.eatWhatWithSSR();
                    foodBuilder.append(food[0]).append("\n");
                    switch (food[1]) {
                        case "1":
                            foodClaass.addOne(simpleMsg.getFromClient());
                            break;
                        case "2":
                            hasSSR = true;
                            foodClaass.addOne(simpleMsg.getFromClient());
                            break;
                        case "3":
                            hasSSR = true;
                            foodClaass.resetTimes(simpleMsg.getFromClient());
                            break;
                    }
                }
            }

            if (foodClaass.getTimes(simpleMsg.getFromClient()) == 99) {
                foodClaass.resetTimes(simpleMsg.getFromClient());
                String[] food = foodUtil.eatGuaranteed(3);
                foodBuilder.append(food[0]);
            } else {
                if (!hasSSR) {
                    String[] food = foodUtil.eatGuaranteed(2);
                    foodBuilder.append(food[0]);

                    if (food[1].equals("3")) {
                        foodClaass.resetTimes(simpleMsg.getFromClient());
                    } else {
                        foodClaass.addOne(simpleMsg.getFromClient());
                    }

                } else {
                    String[] food = foodUtil.eatWhatWithSSR();
                    foodBuilder.append(food[0]);
                    switch (food[1]) {
                        case "1":
                            foodClaass.addOne(simpleMsg.getFromClient());
                            break;
                        case "2":
                            hasSSR = true;
                            foodClaass.addOne(simpleMsg.getFromClient());
                            break;
                        case "3":
                            hasSSR = true;
                            foodClaass.resetTimes(simpleMsg.getFromClient());
                            break;
                    }
                }
            }
            zibenbot.replyMsg(simpleMsg, foodBuilder.toString());
            ConfigLoader.save(zibenbot.appDirectory + "/foodData.json", FoodClaass.class, foodClaass);
        }
    }
}
