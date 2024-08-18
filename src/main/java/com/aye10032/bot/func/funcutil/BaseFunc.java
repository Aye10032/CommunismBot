package com.aye10032.bot.func.funcutil;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.Zibenbot;

/**
 * 方法的基础类 所有方法只需要继承了IFunc
 * 并且通过注解 @Service 进行标记
 * 即可自动注入
 *
 * @author Dazo66
 */
public abstract class BaseFunc implements IFunc {

    public BaseBot bot;
    protected String appDirectory;


    public BaseFunc(BaseBot bot) {
        this.bot = bot;
        if (bot == null) {
            appDirectory = "";
        } else {
            appDirectory = bot.getAppDirectory();
        }
    }

    /**
     * 根据传入的消息，回复消息
     *
     * @param fromMsg 从哪来的什么消息
     * @param msg     要回复的内容
     */
    public void replyMsg(SimpleMsg fromMsg, String msg) {
        if (bot != null) {
            bot.replyMsg(fromMsg, msg);
        } else {
            System.out.println(msg);
        }
    }

    /**
     * 根据传入的消息，引用回复消息
     *
     * @param fromMsg 从哪来的什么消息
     * @param msg     要回复的内容
     */
    public void replyMsgWithQuote(SimpleMsg fromMsg, String msg) {
        if (bot != null) {
            bot.replyMsgWithQuote(fromMsg, msg);
        } else {
            System.out.println(msg);
        }
    }

}
