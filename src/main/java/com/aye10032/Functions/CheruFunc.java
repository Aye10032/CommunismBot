package com.aye10032.Functions;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Utils.CheruUtil;
import com.aye10032.Zibenbot;

public class CheruFunc extends BaseFunc {

    private CheruUtil cheruUtil;

    public CheruFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        cheruUtil = new CheruUtil();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        if (simpleMsg.getMsg().startsWith("切噜") && (simpleMsg.getMsg().split(" ").length == 2)) {
            String msg = "";
            try {
                msg = cheruUtil.toCheru(simpleMsg.getMsg().split(" ")[1]);
            } catch (Exception e) {
                msg = e.toString();
            }
            zibenbot.replyMsg(simpleMsg, msg);
        } else if (simpleMsg.getMsg().startsWith("切噜～")) {
            String msg = simpleMsg.getMsg();
            try {
                msg = cheruUtil.toStr(msg);
            } catch (Exception e) {
                msg = e.toString();
            }
            zibenbot.replyMsg(simpleMsg, msg);
        }
    }
}
