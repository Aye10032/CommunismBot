package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import org.springframework.stereotype.Service;

@Service
public class GbferKillerFunc extends BaseFunc {

    public GbferKillerFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        if (simpleMsg.getMsg().contains("granbluefantasy")) {
            zibenbot.muteMember(simpleMsg.getFromGroup(), simpleMsg.getFromClient(), 114);
            zibenbot.replyMsgWithQuote(simpleMsg, "空骑士爪巴" + zibenbot.getImg(appDirectory + "/gbfKiller.jpg"));
        }
    }
}
