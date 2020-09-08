package com.aye10032.Functions;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Utils.MHWUtil;
import com.aye10032.Zibenbot;

/**
 * @author Dazo66
 */
public class DianGuaiFunc extends BaseFunc {

    MHWUtil mhwUtil;

    public DianGuaiFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        mhwUtil = new MHWUtil();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        if (simpleMsg.getMsg().startsWith("点怪")) {
            if(simpleMsg.getMsg().contains("冰原")){
                String aim = mhwUtil.getIceAim();
                zibenbot.replyMsg(simpleMsg, aim);
            }else {
                String aim = mhwUtil.getAim();
                zibenbot.replyMsg(simpleMsg, aim);
            }
        }
    }
}
