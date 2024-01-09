package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.base.EatData;
import com.aye10032.foundation.utils.ArrayUtils;
import com.aye10032.foundation.utils.ConfigLoader;
import com.aye10032.foundation.utils.FoodUtil;
import com.aye10032.foundation.utils.RandomUtil;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.aye10032.foundation.utils.food.FoodClaass;
import org.springframework.stereotype.Service;

import static com.aye10032.foundation.utils.RandomUtil.getRandom;
import static com.aye10032.foundation.utils.RandomUtil.getRandomWithSSR;

/**
 * @author Aye10032
 */
@Service
public class EatFunc extends BaseFunc {

    private FoodUtil foodUtil;
    private EatData eatData;
    FoodClaass foodClaass = ConfigLoader.load(zibenbot.appDirectory + "/foodData.json", FoodClaass.class);
    private Commander<SimpleMsg> commander;

    public EatFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".泡面"::equals)
                .run((cqmsg) -> {
                    String[] total_list = ArrayUtils.concatAll(
                            eatData.getTongyi(), eatData.getKangshifu(), eatData.getHeweidao(), eatData.getHeweidao());
                    zibenbot.replyMsg(cqmsg, getRandom(total_list));
                })
                .next()
                .or("统一"::equals)
                .run((cqmsg) -> {
                    zibenbot.replyMsg(cqmsg, getRandom(eatData.getTongyi()) + "面");
                })
                .or("康师傅"::equals)
                .run((cqmsg) -> {
                    zibenbot.replyMsg(cqmsg, getRandom(eatData.getKangshifu()) + "面");
                })
                .or("合味道"::equals)
                .run((cqmsg) -> {
                    zibenbot.replyMsg(cqmsg, getRandom(eatData.getHeweidao()) + "面");
                })
                .or("汤达人"::equals)
                .run((cqmsg) -> {
                    zibenbot.replyMsg(cqmsg, getRandom(eatData.getTangdaren()) + "面");
                })
                .pop()
                .or(".一食堂"::equals)
                .run((cqmsg) -> {
                    if (cqmsg.getFromGroup() == 792666782L || cqmsg.getFromGroup() == 295904863L) {
                        zibenbot.replyMsg(cqmsg, getRandomWithSSR(
                                eatData.getCanteen1(), eatData.getCanteen1sr(), eatData.getCanteen1ssr(), 30, 100)[0]);
                    }
                })
                .or(".晚饭"::equals)
                .run((cqmsg) -> {
                    zibenbot.replyMsg(cqmsg, getRandomWithSSR(
                            eatData.getMainlist(), eatData.getSrList(), eatData.getSsrList(), 30, 100)[0]);
                })
                .build();
    }

    @Override
    public void setUp() {
        this.foodUtil = new FoodUtil();
        this.eatData = new EatData();
    }

    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }
}
