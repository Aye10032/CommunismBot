package com.aye10032.utils.fund;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FundingUtils {

    public static List<FundingDetail> getAllFundingStatus(List<String> fundcodes, String startDate, OkHttpClient client) {
        List<FundingDetail> list = new ArrayList<>();
        fundcodes.parallelStream().forEach(
            fundcode -> list.add(getFundingStatus(fundcode, startDate, client))

        );
        return list;
    }

    public static FundingDetail getFundingStatus(String fundcode, String startDate, OkHttpClient client) {
        Request officialWebsiteRequest = new Request.Builder()
            .url(String.format("https://api.doctorxiong.club/v1/fund/detail?code=%s&startDate=%s", fundcode, startDate))
            .method("GET", null)
            .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
            .build();
        Response execute = null;
        String string;
        try {
            execute = client.newCall(officialWebsiteRequest).execute();
            string = execute.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(string).getAsJsonObject();
        JsonObject data = object.get("data").getAsJsonObject();
        Gson gson = new Gson();
        return gson.fromJson(data, FundingDetail.class);
    }


}
