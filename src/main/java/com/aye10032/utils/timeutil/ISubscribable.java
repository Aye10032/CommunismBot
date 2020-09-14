package com.aye10032.Utils.TimeUtil;

import com.aye10032.Functions.funcutil.SimpleMsg;

import java.util.Date;
import java.util.List;

/**
 * 可订阅的接口类
 * @author Dazo66
 */
public interface ISubscribable extends ITimeAdapter, Runnable {

    /**
     * 设置收件人 一般由控制器调用
     * @param simpleMsgs list
     */
     void setRecipients(List<SimpleMsg> simpleMsgs);

    /**
     * 返回一个全局唯一的名字
     * 不推荐在子类中重写
     * 子类推荐用抽象类 在创建对象的时候用匿名内部类的方法重写
     * 这样保证每个对象的name都不一样
     * @return name
     */
     String getName();

    /**
     * 返回自 date 之后的下一个运行时间
     * 不包括 date
     *
     * @param date
     * @return
     */
    @Override
    Date getNextTime(Date date);

    /**
     * 主运行方法 在时间到后会呼叫
     * 在这里进行回复各个收件人
     * @retrun void
     */
    @Override
    void run();

    /**
     * 得到所有收件人
     * @return list
     */
    List<SimpleMsg> getRecipients();

}
