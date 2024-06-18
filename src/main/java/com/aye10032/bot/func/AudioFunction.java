package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @program: communismbot
 * @className: VideoFunction
 * @Description: 语音自动倒放功能
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/10/1 下午 8:28
 */
@Service
@Slf4j
public class AudioFunction extends BaseFunc {

    private Commander<SimpleMsg> commander;
    private OkHttpClient client;

    public AudioFunction(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(this::isAudio)
                .run((msg) -> {
                    if (msg.getFromClient() != 2155231604L) {
                        String filename = zibenbot.getAudioFromMsg(msg).getName();
                        log.info("下载音频文件" + filename);
                        try {
                            MediaType mediaType = MediaType.parse("text/plain");
                            RequestBody requestBody = RequestBody.create(mediaType,"");
                            Request request = new Request.Builder()
                                    .url("http://127.0.0.1:5000/daofang?filename=" + filename)
                                    .method("POST", requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String body = null;
                            if (response.body() != null) {
                                body = new String(response.body().bytes());
                                JsonElement element = JsonParser.parseString(body);

                                if (element.isJsonObject()) {
                                    JsonObject jsonObject = element.getAsJsonObject();
                                    int code = jsonObject.get("code").getAsInt();
                                    if (code == 201) {
                                        File file = new File(appDirectory + "/HuoZiYinShua/daofang.wav");
                                        zibenbot.replyMsg(msg, "来点大家想看的东西");
                                        zibenbot.replyAudio(msg, file);
                                    }
                                }
                            }
                            File file = new File(appDirectory + "/HuoZiYinShua/audio");
                            FileUtils.deleteDirectory(file);
                            file.mkdirs();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .build();
    }

    @Override
    public void setUp() {
        File file = new File(appDirectory + "/HuoZiYinShua/audio");
        if (!file.exists()) {
            file.mkdirs();
        }
        client = new OkHttpClient().newBuilder().build();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

    private boolean isAudio(String msg) {

        return msg.startsWith("[mirai:audio:");
    }
}
