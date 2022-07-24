package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.SetuUtil;
import org.springframework.stereotype.Service;

@Service
public class PixivFunc extends BaseFunc {

    public PixivFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        if (simpleMsg.getMsg().equals(".p站") || simpleMsg.getMsg().equals(".pixiv") || simpleMsg.getMsg().equals(".P站")) {
            if (simpleMsg.isTeamspealMsg()) {
                zibenbot.replyMsg(simpleMsg, "ts频道无法发图片，请从群聊或者私聊获取");
                return;
            }
            zibenbot.replyMsg(simpleMsg, zibenbot.getImg(new SetuUtil(appDirectory).getImage()));
        }
    }
}
