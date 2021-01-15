package com.aye10032.functions.funcutil;

import com.aye10032.Zibenbot;

/**
 * 基础的func的类 包装了一些可用的方法
 *
 * @see FuncFactory  工厂注解类 用于声明这个方法所需要的工厂
 * @see FuncField    字段注解类 用于声明这个字段需要自动注入对象
 *                   应用范围仅限于Zibenbot
 * @see UnloadFunc   方法类注解类 声明了之后告诉加载器不要加载这个类
 * @see IFuncFactory 工厂接口类 如果需要工厂进行对象创建 可以继承这个类
 * @see IFunc        方法接口类
 * @see BaseFunc     抽限方法类 推荐继承这个来创建新的方法
 * @see FuncLoader   方法加载类 用于加载IFunc
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
}
