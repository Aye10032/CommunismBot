package com.aye10032.service.impl;

import com.aye10032.service.LLMService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

/**
 * @program: communismbot
 * @description: 大模型功能接口
 * @author: Aye10032
 * @create: 2024-05-06 16:19
 **/

@Service
public class LLMServiceImpl implements LLMService {

    private static final String SYSTEM_MESSAGE = "你的名字是Moss，是Aye10032基于gpt-oss-120b模型微调的的AI小助手";

    private final ChatClient chatClient;

    public LLMServiceImpl(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    @Override
    public String chat(String prompt) {
        return chatClient.prompt()
                .system(SYSTEM_MESSAGE)
                .user(prompt)
                .call()
                .content();
    }
}
