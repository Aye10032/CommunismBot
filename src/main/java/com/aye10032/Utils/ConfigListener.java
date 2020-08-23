package com.aye10032.Utils;

import java.util.EventListener;

/**
 * @author Dazo66
 */
public class ConfigListener implements EventListener {

    String key;
    Runnable runnable;

    public ConfigListener(String key, Runnable runnable) {
        this.key = key;
        this.runnable = runnable;
    }

    public String getKey() {
        return key;
    }

    public void run(){
        runnable.run();
    }


}
