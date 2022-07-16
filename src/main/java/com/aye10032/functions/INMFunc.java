package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;

public class INMFunc extends BaseFunc {

    public INMFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg CQmsg) {
        String s = CQmsg.getMsg();
        if (s.contains("目力")) {
            zibenbot.replyMsg(CQmsg, zibenbot.getVoice(appDirectory + "/inm/目力.amr"));
        } else if (s.equals("sodayo") || s.equals("是啊") || s.equals("救世啊")) {
            zibenbot.replyMsg(CQmsg, zibenbot.getVoice(appDirectory + "/inm/sodayo.amr"));
        } else if (s.contains("压力马斯内") || s.equals("确实") || s.equals("压力吗死内")) {
            zibenbot.replyMsg(CQmsg, zibenbot.getVoice(appDirectory + "/inm/压力马斯内.amr"));
        }
    }
}
