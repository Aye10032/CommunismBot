package com.aye10032.utils.video;

import com.aye10032.Zibenbot;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class LiveInfo {

    protected String liveapi = "https://api.live.bilibili.com/room/v1/Room/get_info?id=";
    protected String userapi = "https://api.bilibili.com/x/space/acc/info?mid=";

    private int code = 0;
    private String live_url = "https://live.bilibili.com/";
    private String live_title = "";
    private String live_background_url = "";
    private int uid = 0;
    private boolean is_living = false;
    private String live_time = "";
    private Date live_date = null;

    private boolean hasLive = true;

    public LiveInfo(String live_id) {
        String url = liveapi + live_id;

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
                this.live_url += dataJson.get("room_id").getAsString();
                this.live_title = dataJson.get("title").getAsString();
                this.live_background_url = dataJson.get("user_cover").getAsString();
                this.live_time = dataJson.get("live_time").getAsString();

                if (dataJson.get("live_status").getAsInt() == 1) {
                    this.is_living = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean HasLive() {
        return hasLive;
    }

    public boolean Is_living() {
        return is_living;
    }

    public int getUid() {
        return uid;
    }

    public String getLive_url() {
        return live_url;
    }

    public String getLive_title() {
        return live_title;
    }

    public String getLive_background_url() {
        return live_background_url;
    }

    public String getLive_time() {
        return live_time;
    }

    public Date getLive_date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.live_date = sdf.parse(getLive_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return live_date;
    }

    public String getNickName(int uid) {
        String url = this.userapi + uid + "&jsonp=jsonp";
        System.out.println(url);
        String body = "";
        String name = "";
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

            name = JsonParser.parseString(body).getAsJsonObject().get("data").getAsJsonObject().get("name").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }
}
