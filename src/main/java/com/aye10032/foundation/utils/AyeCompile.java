package com.aye10032.foundation.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AyeCompile {

    private static RequestConfig config = RequestConfig.custom().setConnectTimeout(50000).setConnectionRequestTimeout(10000).setSocketTimeout(50000).setRedirectsEnabled(false).build();
    private static CloseableHttpClient httpClient = HttpClients.custom().setMaxConnTotal(2).setDefaultRequestConfig(config).build();
    private String msg = "";
    private boolean hasCode = false;

    private static Pattern av_pattern = Pattern.compile("([aA])([vV])\\d+");
    private Matcher av_matcher;

    private static Pattern bv_pattern = Pattern.compile("([bB])([vV])[(0-9)|(A-Z)|(a-z)]{10}\\b");
    private static List<Pattern> short_site_pattern_list = new ArrayList<>();
    private Matcher bv_matcher;

    static {
        short_site_pattern_list.add(Pattern.compile("https://b23.tv/[(0-9)|(A-Z)|(a-z)]{6,7}\\b"));
        short_site_pattern_list.add(Pattern.compile("https://b.acg.tv/[(0-9)|(A-Z)|(a-z)]{6,7}\\b"));
    }

    public AyeCompile(String msg) {
        this.msg = msg;
        boolean flag = false;
        for (Pattern p : short_site_pattern_list) {
            Matcher matcher = p.matcher(msg);
            if (matcher.find()) {
                msg = matcher.group();
                flag = true;
                break;
            }
        }
        if (flag) {
            try (CloseableHttpResponse conn = httpClient.execute(new HttpGet(msg))) {
                this.msg = conn.getHeaders("Location")[0].getValue();
            } catch (Exception e) {
                //ignore
            }

        }
        av_matcher = av_pattern.matcher(this.msg);
        bv_matcher = bv_pattern.matcher(this.msg);
    }

    public boolean hasAV() {
        this.hasCode = av_matcher.find();
        av_matcher.reset();
        return hasCode;
    }

    public boolean hasBV() {
        this.hasCode = bv_matcher.find();
        bv_matcher.reset();
        return hasCode;
    }

    public String getAVString() {
        return getMatchString(av_matcher);
    }

    public String getBVString() {
        return getMatchString(bv_matcher);
    }

    private String getMatchString(Matcher bv_matcher) {
        String avn = "";
        bv_matcher.find();
        avn = bv_matcher.group();
        bv_matcher.reset();
        return avn;
    }

}
