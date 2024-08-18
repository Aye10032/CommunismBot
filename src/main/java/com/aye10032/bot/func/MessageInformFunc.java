package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import org.springframework.stereotype.Service;

@Service
public class MessageInformFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;

    public MessageInformFunc(BaseBot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(this::containsMe)
                .run((msg) -> {
                    if (msg.getFromClient() != 2375985957L && msg.getFromClient() != 2155231604L  && msg.isGroupMsg()) {
                        String text = "来自群【" + zibenbot.getGroupName(msg.getFromGroup()) + "】的消息\n" +
                                zibenbot.getUserName(msg.getFromClient()) + "[" + msg.getFromClient() + "]：" +
                                msg.getMsg();
                        zibenbot.toPrivateMsg(2375985957L, text);
                    }
                })
                .build();
    }

    @Override
    public void setUp() {
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

    private boolean containsMe(String msg) {
        return msg.contains("aye") || msg.contains("Aye") || msg.contains("阿叶")
                || msg.contains("小叶") || msg.contains("叶受") || msg.contains("叶哥哥");
    }
}
