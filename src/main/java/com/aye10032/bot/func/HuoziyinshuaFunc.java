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
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @program: communismbot
 * @className: HuoziyinshuaFunc
 * @Description: 活字印刷
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/9/4 上午 10:18
 */
@Service
public class HuoziyinshuaFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;
    private OkHttpClient client;

    public HuoziyinshuaFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".活字印刷"::equals)
                .next()
                .or(s -> true)
                .run((msg) -> {
                    String[] msgs = msg.getCommandPieces();
                    try {
                        MediaType mediaType = MediaType.parse("text/plain");
                        RequestBody requestBody = RequestBody.create(mediaType, "");
                        Request request = new Request.Builder()
                                .url("http://127.0.0.1:5000/yinshua?text=" + msgs[1])
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
                                    File file = new File(appDirectory + "/HuoZiYinShua/Output.wav");
                                    zibenbot.replyAudio(msg, file);
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .build();
    }

    @Override
    public void setUp() {
        client = new OkHttpClient().newBuilder().build();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }
}
