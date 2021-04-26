package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ExceptionUtils;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.timeutil.TimeUtils;
import javafx.util.Pair;
import okhttp3.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Dazo66
 */
public class ArkWebTask extends SubscribableBase {

    public ArkWebTask(Zibenbot zibenbot) {
        super(zibenbot);
    }


    @Override
    public String getName() {
        return "舟游网页订阅";
    }

    @Override
    public Date getNextTime(Date date) {
        if (date.getTime() > 1621281540000L) {
            return new Date(date.getTime() + 10000000000000L);
        }
        return TimeUtils.getNextSpecialTime(date, -1, -1, 10, 0, 0, 0);
    }

    @Override
    public Pair<Boolean, String> argsCheck(String[] args) {
        if (args != null && args.length == 1 && args[0].length() > 200) {
            return new Pair<>(true, "订阅成功");
        }

        return new Pair<>(false, "参数不合法 必须一个长度大于200的密钥");
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {

        if (args != null && args.length == 1) {
            try {
                int i = 0;
                String s1 = "";
                try {
                    s1 = arkWebDaily(args[0]);
                } catch (Exception e) {
                    Zibenbot.logWarningStatic(ExceptionUtils.printStack(e));
                }
                if (s1.contains("\"message\":\"\"")) {
                    i++;
                }
                Thread.sleep(1000L);
                String s2 = "";
                try {
                    s2 = arkWebDaily(args[0]);
                } catch (Exception e) {
                    Zibenbot.logWarningStatic(ExceptionUtils.printStack(e));
                }
                if (s2.contains("\"message\":\"\"")) {
                    i++;
                }
                Thread.sleep(1000L);
                replyAll(recivers, String.format("舟游网页活动触发成功 x %d", i));
                try {
                    share(args[0]);
                } catch (Exception e) {
                    Zibenbot.logWarningStatic(ExceptionUtils.printStack(e));
                }
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static String arkWebDaily(String ak2nda) throws IOException {
        OkHttpClient client = Zibenbot.getOkHttpClient();

        Request request = new Request.Builder().url("https://ak.hypergryph.com/activity/preparation/activity/roll")
                .addHeader("accept", "application/json")
                .addHeader("accept-encoding", "gzip, deflate, br")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                .addHeader("cache-control", "no-cache")
                .addHeader("content-length", "21")
                .addHeader("content-type", "application/json;charset=UTF-8")
                .addHeader("cookie", String.format("ak2nda=%s", ak2nda))
                .addHeader("origin", "https://ak.hypergryph.com")
                .addHeader("pragma", "no-cache")
                .addHeader("referer", "https://ak.hypergryph.com/activity/preparation?source=bilibili")
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.72 Safari/537.36")
                .method("POST", RequestBody.create(MediaType.parse("application/json"), "{\"source\":\"bilibili\"}"))
                .build();
        Call call = client.newCall(request);
        String s = call.execute().body().string();
        Zibenbot.logDebugStatic(s);
        return s;
    }

    private static String share(String ak2nda) throws IOException {
        // https://ak.hypergryph.com/activity/preparation/activity/share
        OkHttpClient client = Zibenbot.getOkHttpClient();
        Request request = new Request.Builder().url("https://ak.hypergryph.com/activity/preparation/activity/share")
                .addHeader("accept", "application/json")
                .addHeader("accept-encoding", "gzip, deflate, br")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                .addHeader("cache-control", "no-cache")
                .addHeader("content-length", "21")
                .addHeader("content-type", "application/json;charset=UTF-8")
                .addHeader("cookie", String.format("ak2nda=%s", ak2nda))
                .addHeader("origin", "https://ak.hypergryph.com")
                .addHeader("pragma", "no-cache")
                .addHeader("referer", "https://ak.hypergryph.com/activity/preparation?source=bilibili")
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.72 Safari/537.36")
                .method("POST", RequestBody.create(MediaType.parse("application/json"), "{\"method\":1,\"source\":\"bilibili\"} "))
                .build();
        Call call = client.newCall(request);
        String s = call.execute().body().string();
        Zibenbot.logDebugStatic(s);
        return s;
    }
}
