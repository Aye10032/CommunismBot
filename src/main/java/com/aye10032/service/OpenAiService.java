package com.aye10032.service;

import com.aye10032.entity.AiResult;
import com.aye10032.entity.ChatContext;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 10:42
 **/
public interface OpenAiService {
    AiResult chatGpt(String moduleType, ChatContext chatContext);
    AiResult chatGptStream(String moduleType, ChatContext chatContext);
}
