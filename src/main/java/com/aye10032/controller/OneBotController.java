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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
                ResultVO<QQFriendRequestEvent> resultVO = JSONUtil.json2entity(json, QQFriendRequestEvent.class);
                zibenbot.onFriendEvent(resultVO.getData());
            }
        }

        return Result.success("ok");

    }


}
