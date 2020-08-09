package com.aye10032.Functions;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Utils.SetuUtil;
import com.aye10032.Zibenbot;

public class PixivFunc extends BaseFunc {

    public PixivFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    public void setUp() {

    }

    public void run(SimpleMsg CQmsg) {
        if (CQmsg.getMsg().equals(".p站") || CQmsg.getMsg().equals(".pixiv") || CQmsg.getMsg().equals(".P站")) {
            if (CQmsg.isTeamspealMsg()) {
                zibenbot.replyMsg(CQmsg, "ts频道无法发图片，请从群聊或者私聊获取");
                return;
            }
            zibenbot.replyMsg(CQmsg, zibenbot.getMiraiImg(new SetuUtil(zibenbot.appDirectory).getImage()));
        }
    }
}
