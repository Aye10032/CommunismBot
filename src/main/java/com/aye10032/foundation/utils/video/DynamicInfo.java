package com.aye10032.foundation.utils.video;

import com.aye10032.bot.Zibenbot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.aye10032.foundation.utils.video.BiliData.*;

/**
 * @program: communismbot
 * @className: DynamicInfo
 * @Description: B站动态工具类
 * @version: v1.0
 * @author: Aye10032
 * @date: 2023/3/26 上午 12:45
 */
@Slf4j
public class DynamicInfo {

    private List<Dynamic> dynamics = new ArrayList<>();

    public DynamicInfo(String mid){
        OkHttpClient client = Zibenbot.getOkHttpClient();
        String body = "";

        Request request = new Request.Builder()
                .url(DYNAMIC_API_1 + mid + DYNAMIC_API_2)
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                .method("GET", null)
                .build();

        try(Response response = client.newBuilder().callTimeout(30, TimeUnit.SECONDS).build().newCall(request).execute()){
            if (response.body() != null) {
                body = new String(Objects.requireNonNull(response.body()).bytes());
            }
            JsonArray dynamics = JsonParser.parseString(body).getAsJsonObject().getAsJsonObject("data").getAsJsonArray("items");

            for (JsonElement _dynamic : dynamics) {
                JsonObject dynamic_object = _dynamic.getAsJsonObject();
                if (dynamic_object.get("type").getAsString().equals(TYPE_DRAW) ) {
                    Dynamic dynamic = new Dynamic();

                    dynamic.setDynamic_url("https://" + dynamic_object.getAsJsonObject("basic").get("jump_url").getAsString());

                    dynamic.setDynamic_id(dynamic_object.get("id_str").getAsString());

                    dynamic.setPub_time(dynamic_object.getAsJsonObject("modules").getAsJsonObject("module_author")
                            .get("pub_ts").getAsLong());

                    dynamic.setPub_string(dynamic_object.getAsJsonObject("modules").getAsJsonObject("module_author")
                            .get("pub_time").getAsString());

                    dynamic.setText(dynamic_object.getAsJsonObject("modules").getAsJsonObject("module_dynamic")
                            .getAsJsonObject("desc").get("text").getAsString());
                    JsonArray draws = dynamic_object.getAsJsonObject("modules").getAsJsonObject("module_dynamic")
                            .getAsJsonObject("major").getAsJsonObject("draw").getAsJsonArray("items");

                    for (JsonElement _draw : draws) {
                        JsonObject draw = _draw.getAsJsonObject();
                        dynamic.addImg_url_list(draw.get("src").getAsString());
                    }

                    addDynamics(dynamic);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Dynamic> getDynamics() {
        return dynamics;
    }

    public void addDynamics(Dynamic dynamic) {
        this.dynamics.add(dynamic);
    }
}
