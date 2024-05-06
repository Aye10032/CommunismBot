package com.aye10032.service.impl;

import com.alibaba.fastjson.JSON;
import com.aye10032.service.LLMService;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: communismbot
 * @description: 大模型功能接口
 * @author: Aye10032
 * @create: 2024-05-06 16:19
 **/

@Service
public class LLMServiceImpl implements LLMService {
    @Value("${glm.api.key}")
    private String glmKey;

    @Override
    public ModelApiResponse glmInvoke(String moduleType, List<ChatMessage> messages) {
        String requestId = String.format("chat-%d", System.currentTimeMillis());
        ClientV4 client = new ClientV4.Builder(glmKey).build();

        List<ChatMessage> messageChain = new ArrayList<>();
        messageChain.add(
                new ChatMessage(ChatMessageRole.SYSTEM.value(), "你的名字是Moss，是Aye10032基于GLM模型微调的的AI小助手")
        );
        messageChain.addAll(messages);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(moduleType)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messageChain)
                .requestId(requestId)
                .build();

        return client.invokeModelApi(chatCompletionRequest);
    }

    public static void main(String[] args) {
        List<ChatMessage> messages = new ArrayList<>();
        LLMServiceImpl llmService = new LLMServiceImpl();
        llmService.glmKey = "";
        ChatMessage question = new ChatMessage(ChatMessageRole.USER.value(), "什么是生物信息学？");
        messages.add(question);
        ModelApiResponse result = llmService.glmInvoke(Constants.ModelChatGLM4, messages);
        System.out.println(JSON.toJSONString(result));
    }
}
