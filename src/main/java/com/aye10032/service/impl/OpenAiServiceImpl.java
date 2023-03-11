package com.aye10032.service.impl;

import com.aye10032.Zibenbot;
import com.aye10032.entity.AiResult;
import com.aye10032.entity.ChatContext;
import com.aye10032.entity.ChatMessage;
import com.aye10032.entity.ChatRequest;
import com.aye10032.service.OpenAiService;
import com.aye10032.utils.JsonUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 10:42
 **/
@Slf4j
@Service
public class OpenAiServiceImpl implements OpenAiService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private OkHttpClient httpClient = new OkHttpClient();


    public OkHttpClient getOkHttpClient() {
        return httpClient.newBuilder().callTimeout(30, TimeUnit.SECONDS)
                .proxy(Zibenbot.getProxy()).build();
    }
    @Override
    public AiResult chatGpt(String moduleType, ChatContext chatContext) {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setMessages(chatContext.getContext());
        chatRequest.setModel(moduleType);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(JsonUtils.toJson(chatRequest), mediaType);
        Request request = new Request.Builder().url("https://api.openai.com/v1/chat/completions").method("POST", requestBody).header("Authorization", "Bearer " + openaiApiKey).build();
        try {
            Response execute = getOkHttpClient().newCall(request).execute();
            String string = execute.body().string();
            return JsonUtils.fromJson(string, AiResult.class);
        } catch (Exception e) {
            log.error("调用openai失败：", e);
            return null;
        }
    }

    public static void main(String[] args) {
        OpenAiServiceImpl openAiService = new OpenAiServiceImpl();
        openAiService.openaiApiKey = "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";
        ChatContext chatContext = new ChatContext();
        chatContext.setContext(Lists.newArrayList(ChatMessage.of("user", "What is the OpenAI mission?")));
        AiResult aiResult = openAiService.chatGpt("gpt-3.5-turbo", chatContext);
        System.out.println(JsonUtils.toJson(aiResult));
    }

}
