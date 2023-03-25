package com.aye10032.timetask.functions.funcutil;

/**
 * 回复钩子 当有用户回复了有钩子的消息会触发这个
 * @author dazo
 */
public interface IQuoteHook {

    void run(SimpleMsg originMsg, SimpleMsg replyMsg);

}
