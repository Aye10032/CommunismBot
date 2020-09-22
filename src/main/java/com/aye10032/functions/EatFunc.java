package com.aye10032.functions;

import com.aye10032.data.EatData;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.ArrayUtils;
import com.aye10032.utils.ConfigLoader;
import com.aye10032.utils.FoodUtil;
import com.aye10032.utils.RandomUtil;
import com.aye10032.utils.food.FoodClaass;
import com.aye10032.Zibenbot;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;

/**
 * @author Aye10032
 */
public class EatFunc extends BaseFunc {

    private FoodUtil foodUtil;
    private RandomUtil randomUtil;
    private EatData eatData;
//    FoodClaass foodClaass = ConfigLoader.load(zibenbot.appDirectory + "/foodData.json", FoodClaass.class);
    private Commander<SimpleMsg> commander;

    public EatFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or("泡面"::equals)
                .run((cqmsg) -> {
                    String[] total_list = ArrayUtils.concatAll(
                            eatData.getTongyi(), eatData.getKangshifu(), eatData.getHeweidao(), eatData.getHeweidao());
                    zibenbot.replyMsg(cqmsg, randomUtil.getRandom(total_list));
                })
                .next()
                    .or("统一"::equals)
                    .run((cqmsg) -> {
                        zibenbot.replyMsg(cqmsg, randomUtil.getRandom(eatData.getTongyi()) + "面");
                    })
                    .or("康师傅"::equals)
                    .run((cqmsg) -> {
                        zibenbot.replyMsg(cqmsg, randomUtil.getRandom(eatData.getKangshifu()) + "面");
                    })
                    .or("合味道"::equals)
                    .run((cqmsg) -> {
                        zibenbot.replyMsg(cqmsg, randomUtil.getRandom(eatData.getHeweidao()) + "面");
                    })
                    .or("汤达人"::equals)
                    .run((cqmsg) -> {
                        zibenbot.replyMsg(cqmsg, randomUtil.getRandom(eatData.getTangdaren()) + "面");
                    })
                .pop()
                .or("一食堂"::equals)
                .run((cqmsg)->{
                    if (cqmsg.getFromGroup() == 792666782L){
                        zibenbot.replyMsg(cqmsg,randomUtil.getRandomWithSSR(
                                eatData.getCanteen1(),eatData.getCanteen1sr(),eatData.getCanteen1ssr(),30,100)[0]);
                    }
                })
                .build();
    }

    @Override
    public void setUp() {
        this.foodUtil = new FoodUtil();
        this.randomUtil = new RandomUtil();
        this.eatData = new EatData();
    }

    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
        /*if (simpleMsg.getMsg().equals("晚饭")) {
            if (simpleMsg.getFromGroup() == 792666782L) {
                if (foodClaass.getTimes(simpleMsg.getFromClient()) == 99) {
                    foodClaass.resetTimes(simpleMsg.getFromClient());
                    String[] food = foodUtil.eatGuaranteed(3);
                    zibenbot.replyMsg(simpleMsg, food[0]);
                } else {
                    String[] food = foodUtil.eatWhatWithSSR(0);
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
                    String[] food = foodUtil.eatWhatWithSSR(0);
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
                    String[] food = foodUtil.eatWhatWithSSR(0);
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
        } else if (simpleMsg.getFromGroup() == 792666782L && simpleMsg.getMsg().equals("一食堂")) {
            String[] food = foodUtil.eatWhatWithSSR(1);
            zibenbot.replyMsg(simpleMsg, food[0]);
        }*/
    }
}
