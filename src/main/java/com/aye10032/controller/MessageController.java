package com.aye10032.controller;

import com.aye10032.bot.Zibenbot;
import com.aye10032.foundation.entity.dto.Result;
import com.aye10032.service.FFXIVService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Dazo66
 */
@RestController
@Slf4j
public class MessageController {

    @Autowired
    private Zibenbot zibenbot;

    @Autowired
    private FFXIVService service;

    @RequestMapping(value = "send", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<?> sendMessage(Long group, String msg) {
        if (group != null) {
            zibenbot.toGroupMsg(group, msg);
            log.info("向群" + group + "发送了消息：" + msg);
            return Result.success("success");
        } else {
            return new Result<>("400", "群号不可为空", "");
        }
    }

    @PostMapping("/house")
    public Result<?> updateHouse(String name) {
        if (name != null) {
            service.updateHouse(name);
            log.info("更新了" + name + "的房屋信息");
            return Result.success("success");
        } else {
            return new Result<>("400", "id不可为空", "");
        }
    }

/*    @PostMapping("/github")
    public void githubEvent(@RequestBody String data) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm");
        String formattedDateTime = now.format(formatter);
        String filename = formattedDateTime + "-data.json";
        File file = new File(filename);
        FileUtils.writeStringToFile(file, data, StandardCharsets.UTF_8);
    }*/

    @PostMapping("/githubCreat")
    public void creatEvent(@RequestBody String data_string) throws IOException {
        log.info("收到一条github仓库动态");
        String repo = "";
        String pusher = "";
        String time = "";
        JsonObject data = JsonParser.parseString(data_string).getAsJsonObject();
        repo = data.get("repository").getAsJsonObject().get("html_url").getAsString();
        pusher = data.get("sender").getAsJsonObject().get("login").getAsString();
        time = data.get("repository").getAsJsonObject().get("created_at").getAsString();

        if (data.get("action").getAsString().equals("created")) {
            String msg = pusher + "刚刚创建了一个新的仓库：\n" + repo + "\n----------------\n" + time;
            zibenbot.toGroupMsg(1044102726L, msg);
        }

    }

    @PostMapping("/githubPush")
    public void pushEvent(@RequestBody String data_string) throws IOException {
        log.info("收到一条push动态");
        String branch = "";
        String repo = "";
        String pusher = "";
        String commit = "";
        String time = "";
        JsonObject data = JsonParser.parseString(data_string).getAsJsonObject();
        branch = data.get("ref").getAsString().split("/")[2];
        repo = data.get("repository").getAsJsonObject().get("url").getAsString();
        pusher = data.get("pusher").getAsJsonObject().get("name").getAsString();
        commit = data.get("head_commit").getAsJsonObject().get("message").getAsString();
        time = data.get("head_commit").getAsJsonObject().get("timestamp").getAsString();

        String msg = "来自 " + repo + " 的消息：\n"
                + pusher + "刚刚向" + branch + "分支提交了一个更新，内容为：\n"
                + commit + "\n----------------\n" + time;

        zibenbot.toGroupMsg(1044102726L, msg);
    }

}
