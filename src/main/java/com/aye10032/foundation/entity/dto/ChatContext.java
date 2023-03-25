package com.aye10032.foundation.entity.dto;

import com.aye10032.foundation.entity.base.ChatMessage;
import lombok.Data;

import java.util.List;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 11:05
 **/
@Data
public class ChatContext {

    List<ChatMessage> context;

}
