package com.aye10032.foundation.utils.video;

import com.aye10032.bot.Zibenbot;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.aye10032.foundation.utils.video.BiliData.*;

@Slf4j
public class LiveInfo {
    private int code = 0;
    private String liveTitle = "";
    private String liveBackgroundUrl = "";
    private int uid = 0;
    private String live_url = "";
    private boolean isLiving = false;
    private String liveTime = "";
    private Date liveDate = null;

    private boolean hasLive = true;

    public LiveInfo(String liveId) {
        String url = LIVE_API + liveId;

        String body = null;
        try {
            OkHttpClient client = Zibenbot.getOkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                body = new String(Objects.requireNonNull(response.body()).bytes());
            }

            JsonElement element = JsonParser.parseString(Objects.requireNonNull(body));

            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();

                code = jsonObject.get("code").getAsInt();
                if (code != 0) {
                    hasLive = false;
                    return;
                }

                JsonObject dataJson = jsonObject.get("data").getAsJsonObject();
                this.uid = dataJson.get("uid").getAsInt();
                this.live_url = LIVE_URL + dataJson.get("room_id").getAsString();
                this.liveTitle = dataJson.get("title").getAsString();
                this.liveBackgroundUrl = dataJson.get("user_cover").getAsString();
                this.liveTime = dataJson.get("live_time").getAsString();

                if (dataJson.get("live_status").getAsInt() == 1) {
                    this.isLiving = true;
                }
            }
        } catch (Exception e) {
            log.error("检查直播间信息失败：", e);
        }
    }

    public boolean HasLive() {
        return hasLive;
    }

    public boolean Is_living() {
        return isLiving;
    }

    public int getUid() {
        return uid;
    }

    public String getLiveUrl() {
        return live_url;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public String getLiveBackgroundUrl() {
        return liveBackgroundUrl;
    }

    public String getLiveTime() {
        return liveTime;
    }

    public Date getLiveDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.liveDate = sdf.parse(getLiveTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return liveDate;
    }

    public String getNickName(int uid) {
        String url = USER_API + uid + "&jsonp=jsonp";
        String body = "";
        String name = "";
        try {
            OkHttpClient client = Zibenbot.getOkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                    .method("GET", null)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                body = new String(Objects.requireNonNull(response.body()).bytes());
            }

            name = JsonParser.parseString(body).getAsJsonObject().get("data").getAsJsonObject().get("name").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }
}
