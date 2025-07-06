package com.aye10032.foundation.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * HttpClient实现HTTP文件通用下载类
 * maven依赖
 * <dependency>
 * <groupId>org.apache.httpcomponents</groupId>
 * <artifactId>httpclient</artifactId>
 * <version>4.0.1</version>
 * </dependency>
 * 可下载http文件、图片、压缩文件
 * bug：获取response header中Content-Disposition中filename中文乱码问题
 */
public class HttpUtils {

    public static final int cache = 1024;
    public static final boolean isWindows;
    public static final String splash;
    public static final String root;

    static {
        if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
            isWindows = true;
            splash = "\\";
            root = "D:";
        } else {
            isWindows = false;
            splash = "/";
            root = "/search";
        }
    }

    public static String getStringFromNet(String url, OkHttpClient client) throws IOException {
        return IOUtils.toString(getInputStreamFromNet(url, client));
    }

    public static InputStream postInputStreamFromNet(String url, OkHttpClient client, RequestBody body) throws IOException {
        Request request = new Request.Builder().url(url).method("POST", body).build();
        return client.newCall(request).execute().body().byteStream();
    }

    public static InputStream getInputStreamFromNet(String url, OkHttpClient client) throws IOException {
        Request request = new Request.Builder().url(url).method("GET", null).build();
        return client.newCall(request).execute().body().byteStream();
    }

    public static Response getResponseFromNet(String url, OkHttpClient client) throws IOException {
        Request request = new Request.Builder().url(url).method("GET", null).build();
        return client.newCall(request).execute();
    }


    /**
     * 根据url下载文件，保存到filepath中
     *
     * @param url
     * @param filepath
     * @return
     */
    public static void download(String url, String filepath, OkHttpClient client) {
        try {
            InputStream is = getInputStreamFromNet(url, client);
            File file = new File(filepath);
            file.getParentFile().mkdirs();
            FileOutputStream fileout = new FileOutputStream(file);
            write(is, fileout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void write(InputStream in, OutputStream fileout) throws IOException {
        /**
         * 根据实际运行效果 设置缓冲区大小
         */
        byte[] buffer = new byte[cache];
        int ch = 0;
        while ((ch = in.read(buffer)) != -1) {
            fileout.write(buffer, 0, ch);
        }
        fileout.flush();
        in.close();
        fileout.close();
    }

    /**
     * 获取随机文件名
     *
     * @return
     */
    public static String getRandomFileName() {
        return String.valueOf(System.currentTimeMillis());
    }

}
