package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.aye10032.service.LLMService;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
                    List<ChatMessage> messages = new ArrayList<>();
                    ChatMessage question = new ChatMessage(ChatMessageRole.USER.value(), msg.getMsg().substring(5));
                    messages.add(question);
                    ModelApiResponse modelApiResponse = llmService.glmInvoke("glm-4-flash", messages);
                    zibenbot.replyMsg(msg, modelApiResponse.getData().getChoices().get(0).getMessage().getContent().toString());
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
