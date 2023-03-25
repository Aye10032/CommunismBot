package com.aye10032.bot.func.funcutil;

import javax.annotation.PostConstruct;

/**
 * 方法的基础类 所有方法只需要继承了IFunc
 * 并且通过注解 @Service 进行标记
 * 即可自动注入
 *
 * @author Dazo66
 */
public interface IFunc {


    /**
     * 初始化方法 推荐把方法的初始化放在这里
     * 由bot主体在startUp()中调用
     */
    void setUp();

    /**
     * 执行模块
     *
     * @param simpleMsg
     */
    void run(SimpleMsg simpleMsg);

    @PostConstruct
    default void init() {
        setUp();
    }


}
