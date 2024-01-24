package com.aye10032.foundation.entity.dto;

import com.aye10032.foundation.entity.base.ChatMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 10:50
 **/
@Data
public class AiResult {
    public String id;
    public String object;
    public int created;
    public String model;
    public String system_fingerprint;
    public Usage usage;
    public List<Choice> choices;

    @Data
    public static class Choice {
        private int index;
        // 流消息使用这个字段接收
        private ChatMessage message;
        @JsonProperty("finish_reason")
        private String finishReason;
        private ChatMessage delta;
    }

    @Data
    public static class Usage {
        @JsonProperty("prompt_tokens")
        public int promptTokens;
        @JsonProperty("completion_tokens")
        public int completionTokens;
        @JsonProperty("total_tokens")
        public int totalTokens;
    }

}
