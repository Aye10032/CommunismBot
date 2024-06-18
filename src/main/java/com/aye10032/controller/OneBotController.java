package com.aye10032.controller;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.data.ResultVO;
import com.aye10032.foundation.entity.dto.Result;
import com.aye10032.foundation.entity.onebot.*;
import com.aye10032.foundation.utils.JsonUtils;
import com.aye10032.util.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cms.PasswordRecipient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author dazo
 */
@RestController
@RequestMapping("/onebot")
@Slf4j
public class OneBotController {

    @Autowired
    private Zibenbot zibenbot;

    @PostMapping("/event")
    public Result<String> event(@RequestBody String json) throws JsonProcessingException {
        log.info("收到事件：{}", json);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(json);
        if (Objects.equals(actualObj.at("/post_type").asText(), "message")) {
            QQMessageEvent event = null;
            if (Objects.equals(actualObj.at("/message_type").asText(), "group")) {
                event = JsonUtils.fromJson(json, QQGroupMessageEvent.class);

            }
            if (Objects.equals(actualObj.at("/message_type").asText(), "private")) {
                event = JsonUtils.fromJson(json, QQPrivateMessageEvent.class);
            }
            if (event != null) {
                SimpleMsg simpleMsg = new SimpleMsg(event);
                if (simpleMsg.isGroupMsg()) {
                    log.info("收到群消息：[{}]{}: {}", simpleMsg.getFromGroup(), event.getSender().getNickname(), simpleMsg.getMsg());
                } else {
                    log.info("收到私聊消息：[{}]{}: {}", simpleMsg.getFromClient(), event.getSender().getNickname(), simpleMsg.getMsg());
                }
                zibenbot.runFuncs(simpleMsg);
            }
        }
        if (Objects.equals(actualObj.at("/post_type").asText(), "request")) {
            QQRequestEvent event = null;
            if (Objects.equals(actualObj.at("/request_type").asText(), "friend")) {
                QQFriendRequestEvent resultVO = JsonUtils.fromJson(json, QQFriendRequestEvent.class);
                zibenbot.onFriendEvent(resultVO);
            }
        }

        return Result.success("ok");
    }

    @PostMapping("/send")
    public Result<String> sendMessage(@RequestBody SendMessageRequest request) {
        QQMessageEvent event;
        if (request.getIsGroup()) {
            QQGroupMessageEvent event1 = new QQGroupMessageEvent();
            event1.setMessageId(-1);
            event1.setMessageSeq(-1L);
            event1.setGroupId(request.getGroupId());
            event1.setUserId(request.getSendUserId());
            QQSender sender = new QQSender();
            sender.setUserId(request.getSendUserId());
            sender.setNickname("未知");
            event1.setSender(sender);
            event1.setRawMessage(request.getMessage());
            event1.setTime(System.currentTimeMillis());
            event = event1;
        } else {
            QQPrivateMessageEvent event1 = new QQPrivateMessageEvent();
            event1.setMessageId(-1);
            event1.setMessageSeq(-1L);
            event1.setUserId(request.getSendUserId());
            QQSender sender = new QQSender();
            sender.setUserId(request.getSendUserId());
            sender.setNickname("未知");
            event1.setSender(sender);
            event1.setRawMessage(request.getMessage());
            event1.setTime(System.currentTimeMillis());
            event = event1;
        }
        zibenbot.runFuncs(new SimpleMsg(event));
        return Result.success("success");
    }

    @Data
    public static class SendMessageRequest {
        private Boolean isGroup;
        private Long groupId;
        private String message;
        private Long sendUserId;
    }


}
