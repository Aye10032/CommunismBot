package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.Config;
import com.aye10032.foundation.utils.ConfigListener;
import com.aye10032.foundation.utils.ConfigLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dazo66
 */
@Deprecated
public class BotConfigFunc extends BaseFunc {

    private String configFile;
    private Config config;
    private ConfigLoader<Config> loader;
    private List<ConfigListener> listeners = new ArrayList<>();

    public String getConfig(String key, String dafule) {
        return this.config.getWithDafault(key, dafule);
    }

    public BotConfigFunc(Zibenbot zibenbot) {
        super(zibenbot);
        if (zibenbot == null) {
            configFile = "res/bot_config.json";
        } else {
            configFile = zibenbot.appDirectory + "/bot_config.json";
        }
        loader = new ConfigLoader<>(configFile, Config.class);

    }

    public void addListener(ConfigListener listener) {
        listeners.add(listener);
    }

    @Override
    public void setUp() {
        config = loader.load();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        if (simpleMsg.getMsg().startsWith(".setconfig") || simpleMsg.getMsg().startsWith(".setConfig")) {
            String[] strings = simpleMsg.getMsg().split(" ", 3);
            if (strings.length == 3) {
                config.set(strings[1], strings[2]);
                loader.save(config);
                for (ConfigListener listener : listeners) {
                    if (strings[1].equals(listener.getKey())) {
                        try {
                            listener.run();
                        } catch (Exception e) {
                            replyMsg(simpleMsg, "监听器运行出错：" + e.getMessage());
                        }
                        replyMsg(simpleMsg, "已将[" + strings[1] + "]设置为[" + strings[2] + "]");
                    }
                }
            } else {
                replyMsg(simpleMsg, "设置参数不足!");
            }
        }
        if (simpleMsg.getMsg().startsWith(".getconfig") || simpleMsg.getMsg().startsWith(".getConfig")) {
            String[] strings = simpleMsg.getMsg().split(" ", 2);
            if (strings.length == 2) {
                if ("*".equals(strings[1])) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("configs：\n");
                    loop1:
                    for (String key : config.map.keySet()) {
                        builder.append("    [").append(key).append("]=").append(config.get(key));
                        for (ConfigListener listener : listeners) {
                            if (listener.getKey().equals(key)) {
                                builder.append(" (has listener)");
                                break loop1;
                            }
                        }
                        builder.append("\n");
                    }
                    replyMsg(simpleMsg, builder.toString());
                } else {
                    String s = config.get(strings[1]);
                    replyMsg(simpleMsg, "[" + strings[1] + "]=" + (s == null ? "null" : s));
                }
            }
        }
    }

}
