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
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 10:42
 **/
@Slf4j
@Service
public class OpenAiServiceImpl implements OpenAiService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private final OkHttpClient httpClient = new OkHttpClient();


    public OkHttpClient getOkHttpClient() {
        return httpClient.newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .proxy(Zibenbot.getProxy()).build();
    }

    @Override
    public AiResult chatGpt(String moduleType, ChatContext chatContext) {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setMessages(chatContext.getContext().stream().map(ChatRequest.Message::of).collect(Collectors.toList()));
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

    @Override
    public AiResult chatGptStream(String moduleType, ChatContext chatContext) {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setMessages(chatContext.getContext().stream().map(ChatRequest.Message::of).collect(Collectors.toList()));
        chatRequest.setModel(moduleType);
        chatRequest.setStream(true);
        MediaType mediaType = MediaType.parse("text/event-stream");

        RequestBody requestBody = RequestBody.create(JsonUtils.toJson(chatRequest), mediaType);
        Request request = new Request.Builder().url("https://api.openai.com/v1/chat/completions").method("POST", requestBody).header("Authorization", "Bearer " + openaiApiKey).build();
        Object lock = new Object();
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.DAYS)
                    .readTimeout(1, TimeUnit.DAYS)
                    .proxy(Zibenbot.getProxy()).build();
            final String[] message = new String[1];
            final boolean[] failure = new boolean[1];

            RealEventSource realEventSource = new RealEventSource(request, new EventSourceListener() {

                @Override
                public void onEvent(EventSource eventSource, String id, String type, String data) {
                    message[0] = data;
                    log.info(data);
                }

                @Override
                public void onClosed(EventSource eventSource) {
                    synchronized (lock) {
                        lock.notify();
                    }
                }

                @Override
                public void onFailure(EventSource eventSource, Throwable t, Response response) {
                    synchronized (lock) {
                        lock.notify();
                    }
                    failure[0] = true;
                }
            });
            realEventSource.connect(okHttpClient);
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
            if (failure[0]) {
                throw new Exception("openai服务端断开连接");
            }

            //=======================================

            return JsonUtils.fromJson(message[0], AiResult.class);
        } catch (Exception e) {
            log.error("调用openai失败：", e);
            return null;
        }
    }

    public static void main(String[] args) {
        OpenAiServiceImpl openAiService = new OpenAiServiceImpl();
        openAiService.openaiApiKey = "=";
        ChatContext chatContext = new ChatContext();
        chatContext.setContext(Lists.newArrayList(ChatMessage.of("user", "What is the OpenAI mission?")));
//        AiResult aiResult = openAiService.chatGpt("gpt-3.5-turbo", chatContext);
        AiResult aiResult = openAiService.chatGptStream("gpt-3.5-turbo", chatContext);
        System.out.println(JsonUtils.toJson(aiResult));
    }

}
