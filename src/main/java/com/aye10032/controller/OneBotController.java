package com.aye10032.controller;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.dto.Result;
import com.aye10032.foundation.entity.onebot.QQGroupMessageEvent;
import com.aye10032.foundation.entity.onebot.QQMessageEvent;
import com.aye10032.foundation.entity.onebot.QQPrivateMessageEvent;
import com.aye10032.foundation.entity.onebot.QQRequestEvent;
import com.aye10032.foundation.utils.JsonUtils;
import com.aye10032.util.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
@RequestMapping("onebot")
public class OneBotController {

    @Autowired
    private Zibenbot zibenbot;

    @SneakyThrows
    @RequestMapping("event")
    @PostMapping
    public Result<String> event(@RequestBody String json) {
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
                zibenbot.runFuncs(new SimpleMsg(event));
            }
        }
        if (Objects.equals(actualObj.at("/post_type").asText(), "request")) {
            QQRequestEvent event = null;
            if (Objects.equals(actualObj.at("/request_type").asText(), "friend")) {

            }
        }


    }


}
