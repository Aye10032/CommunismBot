package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.base.MHWData;
import com.aye10032.foundation.utils.ArrayUtils;
import com.aye10032.foundation.utils.RandomUtil;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author Dazo66
 */
@Service
public class DianGuaiFunc extends BaseFunc {

    private MHWData mhwData;
    private RandomUtil randomUtil;
    private Commander<SimpleMsg> commander;

    public DianGuaiFunc(BaseBot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".MHW"::equalsIgnoreCase)
                .run((cqmsg) -> {
                    String result = "用" + randomUtil.getRandom(mhwData.getArm()) +
                            "打" + randomUtil.getRandom(mhwData.getMonster());
                    zibenbot.replyMsg(cqmsg, result);
                })
                .or(".MHWI"::equalsIgnoreCase)
                .run((cqmsg) -> {
                    String[] total_list = ArrayUtils.concatAll(mhwData.getMonster(), mhwData.getMonster_ice());
                    String result = "用" + randomUtil.getRandom(mhwData.getArm()) +
                            "打" + randomUtil.getRandom(total_list);
                    zibenbot.replyMsg(cqmsg, result);
                })
                .or(".MHR"::equalsIgnoreCase)
                .run((cqmsg) -> {
                    String result = "用" + randomUtil.getRandom(mhwData.getArm()) +
                            "打" + randomUtil.getRandom(mhwData.getMonster_rise());
                    zibenbot.replyMsg(cqmsg, result);
                })
                .or(".MHRSB"::equalsIgnoreCase)
                .run((cqmsg) -> {
                    String[] total_list = ArrayUtils.concatAll(mhwData.getMonster_rise(), mhwData.getMonster_sunbreak());
                    Random random = new Random();
                    boolean flag = random.nextBoolean();
                    String _fore = flag ? "" : "怪异化的";
                    String result = "用" + randomUtil.getRandom(mhwData.getArm()) +
                            "打" + _fore + randomUtil.getRandom(total_list);
                    zibenbot.replyMsg(cqmsg, result);
                })
                .build();
    }

    @Override
    public void setUp() {
        mhwData = new MHWData();
        randomUtil = new RandomUtil();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }
}
