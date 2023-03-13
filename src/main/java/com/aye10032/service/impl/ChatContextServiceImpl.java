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
        List<ChatMessage> chatMessages = chatMessageMapper.selectList(new LambdaQueryWrapper<ChatMessage>().eq(ChatMessage::getMessageKey, messageKey)
                .orderByDesc(ChatMessage::getId));
        return chatMessages.isEmpty() ? null : chatMessages.get(0);
    }

    public static void main(String[] args) {
        System.out.println((1044102726 + 2155231604L + "我无法回答你的问题，因为孙笑川是虚构人物，他没有真实存在过。如果你想更具体地了解他的故事，建议查阅相关的小说或电影。").hashCode());
    }

}
