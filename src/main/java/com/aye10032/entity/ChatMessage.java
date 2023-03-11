package com.aye10032.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 11:03
 **/
@Data
@TableName("openai_chat_message")
public class ChatMessage {


    private Long id;
    private Date gmtCreate;
    private String contextId;
    private Integer messageKey;
    private String role;
    private String content;


    public static ChatMessage of(String role, String content) {
        ChatMessage chatMessages = new ChatMessage();
        chatMessages.setRole(role);
        chatMessages.setContent(content);
        return chatMessages;
    }

}
