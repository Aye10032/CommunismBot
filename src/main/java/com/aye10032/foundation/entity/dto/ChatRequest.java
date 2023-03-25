package com.aye10032.foundation.entity.dto;

import com.aye10032.foundation.entity.base.ChatMessage;
import lombok.Data;

import java.util.List;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 11:11
 **/
@Data
public class ChatRequest {

    private String model;
    private List<Message> messages;

    private boolean stream;

    @Data
    public static class Message {
        private String role;
        private String content;

        public static Message of(ChatMessage chatMessage) {
            Message message = new Message();
            message.setRole(chatMessage.getRole());
            message.setContent(chatMessage.getContent());
            return message;
        }
    }

}
