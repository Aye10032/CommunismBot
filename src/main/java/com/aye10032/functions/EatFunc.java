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
    FoodClaass foodClaass = ConfigLoader.load(zibenbot.appDirectory + "/foodData.json", FoodClaass.class);
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
                .or("晚饭"::equals)
                .run((cqmsg)->{
                    zibenbot.replyMsg(cqmsg,randomUtil.getRandomWithSSR(
                            eatData.getMainlist(),eatData.getSrList(),eatData.getSsrList(),30,100)[0]);
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
    }
}
