package com.aye10032.functions;

import com.aye10032.data.MHWData;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.Zibenbot;
import com.aye10032.utils.ArrayUtils;
import com.aye10032.utils.RandomUtil;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;

/**
 * @author Dazo66
 */
public class DianGuaiFunc extends BaseFunc {

    private MHWData mhwData;
    private RandomUtil randomUtil;
    private Commander<SimpleMsg> commander;

    public DianGuaiFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or("点怪"::equals)
                .run((cqmsg) -> {
                    String[] total_list = ArrayUtils.concatAll(mhwData.getMonster(), mhwData.getMonster_ice());
                    String result = "用" + randomUtil.getRandom(mhwData.getArm()) +
                            "打" + randomUtil.getRandom(total_list);
                    zibenbot.replyMsg(cqmsg, result);
                })
                .next()
                    .or("冰原"::equals)
                    .run((cqmsg) -> {
                        String result = "用" + randomUtil.getRandom(mhwData.getArm()) +
                                "打" + randomUtil.getRandom(mhwData.getMonster_ice());
                        zibenbot.replyMsg(cqmsg, result);
                    })
                .pop()
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
