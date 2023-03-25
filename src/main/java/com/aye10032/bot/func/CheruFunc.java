package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.CheruUtil;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import org.springframework.stereotype.Service;

@Service
public class CheruFunc extends BaseFunc {

    private CheruUtil cheruUtil;
    private Commander<SimpleMsg> commander;

    public CheruFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        cheruUtil = new CheruUtil();
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".切噜"::equals)
                .next()
                .or(s -> true)
                .run((msg) -> {
                    String reply;
                    try {
                        reply = cheruUtil.toCheru(msg.getMsg());
                    } catch (Exception e) {
                        reply = e.toString();
                    }
                    zibenbot.replyMsg(msg, reply);
                })
                .pop()
                .or(".切噜～"::equals)
                .next()
                .or(s -> true)
                .run((msg) -> {
                    String reply = msg.getMsg();
                    try {
                        reply = cheruUtil.toStr(reply);
                    } catch (Exception e) {
                        reply = e.toString();
                    }
                    zibenbot.replyMsg(msg, reply);
                })
                .pop()
                .build();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }
}
