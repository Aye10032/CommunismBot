package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @program: fsbot
 * @className: RollFunc
 * @Description: roll点功能
 * @version: v1.0
 * @author: Aye10032
 * @date: 2021/8/30 下午 10:21
 */
@Service
public class RollFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;


    public RollFunc(BaseBot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".r2"::equalsIgnoreCase)
                .run((cqmsg) -> {
                    Random random = new Random();
                    int flag = random.nextInt(2) + 1;
                    zibenbot.replyMsg(cqmsg, zibenbot.at(cqmsg.getFromClient()) + " 投出了" + flag + "/2点");
                })
                .or(".r4"::equalsIgnoreCase)
                .run((cqmsg) -> {
                    Random random = new Random();
                    int flag = random.nextInt(4) + 1;
                    zibenbot.replyMsg(cqmsg, zibenbot.at(cqmsg.getFromClient()) + " 投出了" + flag + "/4点");
                })
                .or(".r6"::equalsIgnoreCase)
                .run((cqmsg) -> {
                    Random random = new Random();
                    int flag = random.nextInt(6) + 1;
                    zibenbot.replyMsg(cqmsg, zibenbot.at(cqmsg.getFromClient()) + " 投出了" + flag + "/6点");
                })
                .or(".r8"::equalsIgnoreCase)
                .run((cqmsg) -> {
                    Random random = new Random();
                    int flag = random.nextInt(8) + 1;
                    zibenbot.replyMsg(cqmsg, zibenbot.at(cqmsg.getFromClient()) + " 投出了" + flag + "/8点");
                })
                .or(".r10"::equalsIgnoreCase)
                .run((cqmsg) -> {
                    Random random = new Random();
                    int flag = random.nextInt(10) + 1;
                    zibenbot.replyMsg(cqmsg, zibenbot.at(cqmsg.getFromClient()) + " 投出了" + flag + "/10点");
                })
                .or(".r20"::equalsIgnoreCase)
                .run((cqmsg) -> {
                    Random random = new Random();
                    int flag = random.nextInt(20) + 1;
                    zibenbot.replyMsg(cqmsg, zibenbot.at(cqmsg.getFromClient()) + " 投出了" + flag + "/20点");
                })
                .or(".r100"::equalsIgnoreCase)
                .run((cqmsg) -> {
                    Random random = new Random();
                    int flag = random.nextInt(100) + 1;
                    zibenbot.replyMsg(cqmsg, zibenbot.at(cqmsg.getFromClient()) + " 投出了" + flag + "/100点");
                })
                .build();
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }
}
