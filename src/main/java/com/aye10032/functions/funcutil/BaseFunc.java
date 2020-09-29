package com.aye10032.functions.funcutil;

import com.aye10032.Zibenbot;

/**
 * 基础的func的类 包装了一些可用的方法
 * @author Dazo66
 */
@UnloadFunc
public abstract class BaseFunc implements IFunc {

    public Zibenbot zibenbot;
    protected String appDirectory;


    public BaseFunc(Zibenbot zibenbot) {
        this.zibenbot = zibenbot;
        if (zibenbot == null) {
            appDirectory = "";
        } else {
            appDirectory = zibenbot.appDirectory;
        }
    }

    /**
     * 根据传入的消息，回复消息
     *
     * @param fromMsg 从哪来的什么消息
     * @param msg 要回复的内容
     */
    public void replyMsg(SimpleMsg fromMsg, String msg) {
        if (zibenbot!= null) {
            zibenbot.replyMsg(fromMsg, msg);
        } else {
            System.out.println(msg);
        }

    }
}
