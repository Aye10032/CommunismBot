package com.aye10032.utils.timeutil;

import javafx.util.Pair;

import java.util.Date;
import java.util.List;

/**
 * 可订阅的接口类
 * 可以通过注解类{@link SubConfig} 来配置订阅器
 * 当前可配置的有是否支持用户参数
 * @author Dazo66
 */
public interface ISubscribable extends ITimeAdapter {

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
     * @param date 上一个时间
     * @return 下一次运行的时间
     */
    @Override
    Date getNextTime(Date date);

    /**
     * 主运行方法 在时间到后会呼叫
     * 在这里进行回复各个收件人
     * @param recivers 收件人类 可以通过reciver.getSender()得到SimpleMsg再用Zibenbot发送消息
     * @param args 用户订阅时传入的参数 会传到这里
     *             当订阅器存在注解{@link SubConfig}这个注解类来设置是这个订阅器是否支持自定义参数
     *             如果不支持 那么这个传入的参数永远为null
     * @retrun void
     */
    void run(List<Reciver> recivers, String[] args);

    /**
     * 参数检查 只会在第一次订阅时调用 检查不通过就不进行订阅
     * 如果订阅器不支持用户参数 则这个方法永远不会调用
     *
     * @param args 用户将要订阅的参数
     * @return Pair<Boolean   ,       String> 分别是检查是否通过，如果不通过的提示消息是
     */
    Pair<Boolean, String> argsCheck(String[] args);


}
