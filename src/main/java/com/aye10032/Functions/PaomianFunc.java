package com.aye10032.Functions;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Utils.PaomianUtil;
import com.aye10032.Zibenbot;

public class PaomianFunc extends BaseFunc {

    PaomianUtil paomianUtil;

    public PaomianFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        this.paomianUtil = new PaomianUtil();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        if (simpleMsg.getMsg().equals("泡面")) {
            String paomian = paomianUtil.getType();
            zibenbot.replyMsg(simpleMsg, paomian);
        } else if (simpleMsg.getMsg().startsWith("泡面")) {
            String[] strings = simpleMsg.getMsg().split(" ");
            if (strings.length == 2) {
                String paomian;
                switch (strings[1]) {
                    case "统一":
                        paomian = paomianUtil.getType(1);
                        zibenbot.replyMsg(simpleMsg, paomian);
                        break;
                    case "康师傅":
                        paomian = paomianUtil.getType(2);
                        zibenbot.replyMsg(simpleMsg, paomian);
                        break;
                    case "汤达人":
                        paomian = paomianUtil.getType(3);
                        zibenbot.replyMsg(simpleMsg, paomian);
                        break;
                    case "合味道":
                        paomian = paomianUtil.getType(4);
                        zibenbot.replyMsg(simpleMsg, paomian);
                        break;
                }
            }
        }
    }


}
