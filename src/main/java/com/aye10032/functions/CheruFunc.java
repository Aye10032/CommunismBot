package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.CheruUtil;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
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
            .or("切噜"::equals)
            .next()
            .or(s->true)
            .run((msg)->{
                String reply = "";
                try {
                    reply = cheruUtil.toCheru(msg.getMsg());
                } catch (Exception e) {
                    reply = e.toString();
                }
                zibenbot.replyMsg(msg, reply);
            })
            .pop()
            .or("切噜～"::equals)
            .next()
            .or(s->true)
            .run((msg)->{
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
