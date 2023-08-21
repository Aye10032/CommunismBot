package com.aye10032.controller;

import com.aye10032.bot.Zibenbot;
import com.aye10032.foundation.entity.dto.Result;
import com.aye10032.service.FFXIVService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
    public Result<?> sendMessage(@RequestParam(value = "group", required = true) Long group,
                                 @RequestParam(value = "msg", required = true) String msg) {
        if (group != null) {
            zibenbot.toGroupMsg(group, msg);
            log.info("向群" + group + "发送了消息：" + msg);
            return Result.success("success");
        } else {
            return new Result<>("400", "群号不可为空", "");
        }
    }

    @PostMapping("/house")
    public Result<?> updateHouse(@RequestParam(value = "name", required = true) String name) {
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
            zibenbot.toGroupMsg(456919710L, msg);
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
        repo = data.get("repository").getAsJsonObject().get("url").getAsString();

        if (repo.equals("https://github.com/Redstone-Tech-Reupload-Group/Subtitles")) {
            branch = data.get("ref").getAsString().split("/")[2];
            pusher = data.get("pusher").getAsJsonObject().get("name").getAsString();
            commit = data.get("head_commit").getAsJsonObject().get("message").getAsString();
            time = data.get("head_commit").getAsJsonObject().get("timestamp").getAsString();

            JsonArray add_files_arr = data.get("head_commit").getAsJsonObject().get("added").getAsJsonArray();
            JsonArray remove_files_arr = data.get("head_commit").getAsJsonObject().get("removed").getAsJsonArray();
            JsonArray modified_files_arr = data.get("head_commit").getAsJsonObject().get("modified").getAsJsonArray();
            Gson gson = new Gson();

            StringBuilder msg_builder = new StringBuilder();
            msg_builder.append(pusher).append("刚刚向").append(branch)
                    .append("分支提交了一个更新，内容为：\n").append(commit);

            if (add_files_arr.size() != 0) {
                String[] add_files = gson.fromJson(add_files_arr, String[].class);
                msg_builder.append("\n----------------\n添加了：");
                for (String file : add_files) {
                    msg_builder.append("\n").append(file);
                }
            }
            if (remove_files_arr.size() != 0) {
                String[] remove_files = gson.fromJson(remove_files_arr, String[].class);
                msg_builder.append("\n----------------\n移除了：");
                for (String file : remove_files) {
                    msg_builder.append("\n").append(file);
                }
            }
            if (modified_files_arr.size() != 0) {
                String[] modified_files = gson.fromJson(modified_files_arr, String[].class);
                msg_builder.append("\n----------------\n修改了：");
                for (String file : modified_files) {
                    msg_builder.append("\n").append(file);
                }
            }

            msg_builder.append("\n----------------\n").
                    append(time).append("\n").append(repo);


            zibenbot.toGroupMsg(456919710L, msg_builder.toString());
            zibenbot.toPrivateMsg(2375985957L, msg_builder.toString());
        }

//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm");
//        String formattedDateTime = now.format(formatter);
//        String filename = formattedDateTime + "-data.json";
//        File file = new File(filename);
//        FileUtils.writeStringToFile(file, data.toString(), StandardCharsets.UTF_8);
    }

}
