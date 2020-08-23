package com.aye10032.Functions;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Zibenbot;

import java.io.File;
import java.util.Random;

/**
 * @author Dazo66
 */
public class liantongFunc extends BaseFunc {

    private Random random;

    public liantongFunc(Zibenbot zibenbot) {
        super(zibenbot);
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg CQmsg) {
        if (CQmsg.getMsg().contains("炼铜")) {
            if (CQmsg.isTeamspealMsg()) {
                zibenbot.replyMsg(CQmsg, "ts频道无法发图片，请从群聊或者私聊获取");
                return;
            }
            if (random.nextDouble() < 0.2d) {
                zibenbot.replyMsg(CQmsg, zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\liantong.jpg")));
            }
        }
    }
}
