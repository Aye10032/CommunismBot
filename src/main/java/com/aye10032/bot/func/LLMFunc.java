package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.aye10032.service.LLMService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: communismbot
 * @description: 大模型相关功能
 * @author: Aye10032
 * @create: 2024-05-06 16:13
 **/

@Slf4j
@Service
public class LLMFunc extends BaseFunc {

    @Autowired
    LLMService llmService;

    private Commander<SimpleMsg> commander;

    public LLMFunc(BaseBot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or("moss"::equalsIgnoreCase)
                .next()
                .orArray(s -> true)
                .run(msg -> {
                    String prompt = msg.getMsg().substring(5);
                    String reply = llmService.chat(prompt);
                    zibenbot.replyMsg(msg, reply);
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

    @Override
    public void init() {
        super.init();
    }
}
