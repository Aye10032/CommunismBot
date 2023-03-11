package com.aye10032.service.impl;

import com.aye10032.entity.ChatMessage;
import com.aye10032.mapper.ChatMessageMapper;
import com.aye10032.service.ChatContextService;
import com.aye10032.utils.RandomUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 15:37
 **/
@Service
public class ChatContextServiceImpl implements ChatContextService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    public List<ChatMessage> load(String contextId, Integer limit) {
        LambdaQueryWrapper<ChatMessage> queryWrapper = new LambdaQueryWrapper<ChatMessage>().eq(ChatMessage::getContextId, contextId).orderByDesc(ChatMessage::getId);
        List<ChatMessage> chatMessages = chatMessageMapper.selectPage(Page.of(1, limit.longValue()), queryWrapper).getRecords();
        return Lists.reverse(chatMessages);
    }

    @Override
    public List<ChatMessage> load(String contextId) {
        return load(contextId, Integer.MAX_VALUE);
    }

    @Override
    public String newContext(ChatMessage chatMessage) {
        String contextId = RandomUtils.getRandNumberString();
        chatMessage.setContextId(contextId);
        chatMessage.setGmtCreate(new Date());
        chatMessageMapper.insert(chatMessage);
        return contextId;
    }

    @Override
    public boolean push(String contextId, ChatMessage chatMessage) {
        chatMessage.setContextId(contextId);
        chatMessage.setGmtCreate(new Date());
        return chatMessageMapper.insert(chatMessage) == 1;
    }

    @Override
    public ChatMessage queryByMessageKey(Integer messageKey) {
        return chatMessageMapper.selectOne(new LambdaQueryWrapper<ChatMessage>().eq(ChatMessage::getMessageKey, messageKey)
                .orderByDesc(ChatMessage::getId));
    }
}
