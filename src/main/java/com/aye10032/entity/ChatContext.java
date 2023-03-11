package com.aye10032.entity;

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
