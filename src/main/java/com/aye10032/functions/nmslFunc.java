package com.aye10032.functions;

import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.Zibenbot;

/**
 * @author Dazo66
 */
public class nmslFunc extends BaseFunc {


    public nmslFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        //pass
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        if (simpleMsg.getMsg().contains("nmsl")) {
            if (simpleMsg.isGroupMsg() || simpleMsg.isPrivateMsg()) {
                zibenbot.replyMsg(simpleMsg, zibenbot.at(simpleMsg.getFromClient()) + simpleMsg.getMsg());
            } else {
                //zibenbot.replyMsg(simpleMsg, "@" + zibenbot.teamspeakBot.api.getClientInfo((int) simpleMsg.fromClient).getLoginName() + simpleMsg.msg);
            }
        }
    }
}
