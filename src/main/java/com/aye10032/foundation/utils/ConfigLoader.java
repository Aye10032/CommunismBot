package com.aye10032.foundation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Type;

public class ConfigLoader<T> {

    private final static String EMPTY_JSON = "{}";

    //直接传入文件和类型进行加载
    //如果文件不存在会返回所有属性为null的对象
    //推荐在拿属性的时候使用 ObjectUtils.defaultIfNull() 方法设置默认值
    //在定义属性的时候 推荐使用对象类型的属性 不使用 int byte char 等
    //以防止出现默认值不为null的情况
    //详情示例参考 {ConfigTest.class}
    public static <T> T load(File file, Type type) {
        return new ConfigLoader<T>(file, type).load();
    }

    public static <T> T load(String file, Type type) {
        return new ConfigLoader<T>(new File(file), type).load();
    }

    public static <T> void save(File file, Type type, T config) {
        new ConfigLoader<T>(file, type).save(config);
    }

    public static <T> void save(String file, Type type, T config) {
        new ConfigLoader<T>(new File(file), type).save(config);
    }

    Gson gson;
    File file;
    Type type;

    public ConfigLoader(String file, Type type) {
        this(new File(file), type);
    }

    public ConfigLoader(File file, Type type) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.file = file;
        this.type = type;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public T load() {
        if (file.exists()) {
            try {
                String s = IOUtils.toString(new FileReader(file));
                s = s.trim().isEmpty() ? EMPTY_JSON : s;
                return gson.fromJson(s, type);
            } catch (Exception e) {
                e.printStackTrace();
                return gson.fromJson(EMPTY_JSON, type);
            }
        } else {
            return gson.fromJson(EMPTY_JSON, type);
        }
    }

    public void save(T config) {
        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            if (!file.isFile()) {
                return;
            }
            String s = gson.toJson(config);
            OutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            IOUtils.write(s, stream);
            stream.flush();
            IOUtils.closeQuietly(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
