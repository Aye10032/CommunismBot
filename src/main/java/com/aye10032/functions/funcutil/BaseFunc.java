package com.aye10032.functions.funcutil;

import com.aye10032.Zibenbot;

/**
 * 方法的基础类 所有方法只需要继承了IFunc
 * 并且通过注解 @Service 进行标记
 * 即可自动注入
 *
 * @author Dazo66
 */
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
        if (zibenbot != null) {
            zibenbot.replyMsg(fromMsg, msg);
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
        if (zibenbot != null) {
            zibenbot.replyMsgWithQuote(fromMsg, msg);
        } else {
            System.out.println(msg);
        }
    }

    /**
     * 回复消息 并且施加回复钩子 如果有用户回复这个消息会触发钩子
     *
     * @param fromMsg 从哪来的什么消息
     * @param msg     要回复的内容
     */
    public void replyMsgWithQuoteHook(SimpleMsg fromMsg, String msg, IQuoteHook hook) {
        if (zibenbot != null) {
            zibenbot.replyMsgWithQuoteHook(fromMsg, msg, hook);
        } else {
            System.out.println(msg);
        }
    }
}
