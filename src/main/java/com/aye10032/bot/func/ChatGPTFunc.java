package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.base.ChatMessage;
import com.aye10032.foundation.entity.dto.AiResult;
import com.aye10032.foundation.entity.dto.ChatContext;
import com.aye10032.service.ChatContextService;
import com.aye10032.service.OpenAiService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 17:56
 **/
@Slf4j
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
    @Transactional
    public void run(SimpleMsg simpleMsg) {
        try {
            if (simpleMsg.getCommandPieces().length > 1) {
                if (simpleMsg.getCommandPieces()[0].equalsIgnoreCase("moss")) {
                    ChatMessage chatMessage = ChatMessage.of("user", simpleMsg.getPlainMsg().substring(4).trim());
                    chatMessage.setMessageKey(simpleMsg.getQuoteKey());
                    String s = chatContextService.newContext(chatMessage);
                    log.info("新发起会话：{}", s);
                    ChatContext chatContext = new ChatContext();
                    chatContext.setContext(Lists.newArrayList(chatMessage));
                    chat(simpleMsg, s, chatContext);
                    return;
                } else if (simpleMsg.getCommandPieces()[0].equalsIgnoreCase("chat")) {
                    replyMsg(simpleMsg, "chat这个前缀太土了，你可以叫我MOSS");
                }
            }
            if (simpleMsg.getQuoteMsg() != null) {
                ChatMessage chatMessage = chatContextService.queryByMessageKey(simpleMsg.getQuoteMsg().getQuoteKey());
                if (chatMessage == null) {
                    return;
                }
                if (chatMessage.getUsed() == Boolean.TRUE) {
                    replyMsg(simpleMsg, "这条语句已经回复过了 不能修改了哦~");
                    return;
                }
                chatContextService.usedMessage(chatMessage.getId());
                String contextId = chatMessage.getContextId();
                log.info("上下文会话：{}", contextId);
                List<ChatMessage> chatMessages = chatContextService.load(contextId);
                chatMessages.add(ChatMessage.of("user", simpleMsg.getPlainMsg().replace("chat ", "")));
                ChatContext chatContext = new ChatContext();
                chatContext.setContext(chatMessages);
                chat(simpleMsg, contextId, chatContext);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }


    }

    private void chat(SimpleMsg simpleMsg, String s, ChatContext chatContext) {
        try {
//            AiResult aiResult = openAiService.chatGpt(GPT_3_5_TURBO, chatContext);
            AiResult aiResult = openAiService.chatGptStream(GPT_3_5_TURBO, chatContext);

            ChatMessage replyMessage = aiResult.getChoices().get(0).getMessage();
            String content = replyMessage.getContent();
            if (content.startsWith("\n\n")) {
                content = content.substring(2);
            }
            replyMessage.setContent(content);
            content = content.replace("\\", "\\\\");
            replyMessage.setMessageKey(SimpleMsg.getQuoteKeyStatic(simpleMsg.getFromGroup(), zibenbot.getBotQQId(), content));
            chatContextService.push(s, replyMessage);
            replyMsg(simpleMsg, content);
        } catch (Exception e) {
            replyMsg(simpleMsg, "调用出错了，请稍后再试。");
            throw new RuntimeException();
        }
    }
}
