package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author dazo
 */
@Service
public class SetEssenceMessage extends BaseFunc {


    public SetEssenceMessage(BaseBot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        Integer messageId = null;
        if (simpleMsg.getMessageSplitResult() != null) {
            for (Map<String, String> map : simpleMsg.getMessageSplitResult()) {
                if ("reply".equals(map.get("CQ"))) {
                    messageId = Integer.parseInt(map.get("id"));
                }
            }
        }
        if (messageId != null && (simpleMsg.getMsg().contains("射精")
                        || simpleMsg.getMsg().contains("设精")
                        || simpleMsg.getMsg().contains("设置精华")
                        || simpleMsg.getMsg().contains("设了"))) {
            bot.setEssenceMsg(messageId);
        }
    }
}
