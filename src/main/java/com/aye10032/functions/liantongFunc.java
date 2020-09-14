package com.aye10032.functions;

import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
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
    public void run(SimpleMsg simpleMsg) {
        if (simpleMsg.getMsg().contains("炼铜")) {
            if (simpleMsg.isTeamspealMsg()) {
                zibenbot.replyMsg(simpleMsg, "ts频道无法发图片，请从群聊或者私聊获取");
                return;
            }
            if (random.nextDouble() < 0.2d) {
                zibenbot.replyMsg(simpleMsg, zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\liantong.jpg")));
            }
        }
    }
}
