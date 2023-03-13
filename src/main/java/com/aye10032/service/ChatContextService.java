package com.aye10032.service;

import com.aye10032.entity.ChatMessage;
import com.aye10032.functions.funcutil.SimpleMsg;

import java.util.List;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 15:21
 **/
public interface ChatContextService {

    /**
     * 加载上下文
     * @param id 上下文id
     * @param limit 限制条数
     * @return 上下文列表
     */
    List<ChatMessage> load(String id, Integer limit);

    /**
     * 加载上下文 不限制条数 默认会加载所有的上下文
     * @param id 上下文id
     * @return 上下文列表
     */
    List<ChatMessage> load(String id);

    /**
     * 创建新的上下文
     * @param chatMessage 开始消息
     * @return 上下文唯一id
     */
    String newContext(ChatMessage chatMessage);

    /**
     * push到指定上下文后面
     * @param chatMessage
     * @return
     */
    boolean push(String id, ChatMessage chatMessage);

    /**
     * 根据message key查询
     * @param messageKey message key
     * @return ChatMessage
     */
    ChatMessage queryByMessageKey(Integer messageKey);


    void usedMessage(Long id);
}
