package com.aye10032.service;

import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;

import java.util.List;

public interface LLMService {

    ModelApiResponse glmInvoke(String moduleType, List<ChatMessage> messages);

}
