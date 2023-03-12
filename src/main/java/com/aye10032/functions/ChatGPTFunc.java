package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.entity.AiResult;
import com.aye10032.entity.ChatContext;
import com.aye10032.entity.ChatMessage;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.service.ChatContextService;
import com.aye10032.service.OpenAiService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 17:56
 **/
@Service
public class ChatGPTFunc extends BaseFunc {

    public static final String GPT_3_5_TURBO = "gpt-3.5-turbo";
    @Autowired
    private ChatContextService chatContextService;
    @Autowired
    private OpenAiService openAiService;

    public ChatGPTFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        if (simpleMsg.getCommandPieces().length > 1) {
            if (simpleMsg.getCommandPieces()[0].equals("chat")) {
                ChatMessage chatMessage = ChatMessage.of("user", simpleMsg.getMsg().substring(4).trim());
                chatMessage.setMessageKey(simpleMsg.getQuoteKey());
                String s = chatContextService.newContext(chatMessage);
                ChatContext chatContext = new ChatContext();
                chatContext.setContext(Lists.newArrayList(chatMessage));
                chat(simpleMsg, s, chatContext);
                return;
            }
        }
        if (simpleMsg.getQuoteMsg() != null) {
            ChatMessage chatMessage = chatContextService.queryByMessageKey(simpleMsg.getQuoteMsg().getQuoteKey());
            if (chatMessage == null) {
                return;
            }
            String contextId = chatMessage.getContextId();
            List<ChatMessage> chatMessages = chatContextService.load(contextId);
            chatMessages.add(ChatMessage.of("user", simpleMsg.getMsg().replace("chat ", "")));
            ChatContext chatContext = new ChatContext();
            chatContext.setContext(chatMessages);
            chat(simpleMsg, contextId, chatContext);
        }

    }

    private void chat(SimpleMsg simpleMsg, String s, ChatContext chatContext) {
        AiResult aiResult = openAiService.chatGpt(GPT_3_5_TURBO, chatContext);
        ChatMessage replyMessage = aiResult.getChoices().get(0).getMessage();
        replyMessage.setMessageKey(SimpleMsg.getQuoteKey(simpleMsg.getFromGroup(), zibenbot.getBotQQId(), simpleMsg.getMsg()));
        chatContextService.push(s, replyMessage);
        replyMsg(simpleMsg, replyMessage.getContent());
    }
}
