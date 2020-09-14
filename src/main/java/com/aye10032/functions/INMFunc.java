/*
package com.aye10032.functions;

import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.Zibenbot;

public class INMFunc extends BaseFunc {
    public INMFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg CQmsg) {
        if (CQmsg.getMsg().contains("目力")){
            zibenbot.replyMsg(CQmsg,zibenbot.getCQCode().record("目力.amr"));
        }else if (CQmsg.getMsg().contains("sodayo")||CQmsg.getMsg().equals("是啊")||CQmsg.getMsg().equals("救世啊")){
            zibenbot.replyMsg(CQmsg,zibenbot.getCQCode().record("sodayo.amr"));
        }else if (CQmsg.getMsg().contains("压力马斯内")||CQmsg.getMsg().equals("确实")||CQmsg.getMsg().equals("压力吗死内")){
            zibenbot.replyMsg(CQmsg,zibenbot.getCQCode().record("压力马斯内.amr"));
        }
    }
}
*/
