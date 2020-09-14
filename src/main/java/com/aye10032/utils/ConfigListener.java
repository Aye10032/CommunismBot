package com.aye10032.utils;

import java.util.EventListener;

/**
 * @author Dazo66
 */
public class ConfigListener implements EventListener {

    private String key;
    private Runnable runnable;

    public ConfigListener(String key, Runnable run) {
        this.key = key;
        this.runnable = run;
    }

    public String getKey() {
        return key;
    }

    public void run() {
        runnable.run();
    }


}
