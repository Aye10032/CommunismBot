package com.aye10032.bot.func.funcutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

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

    @EventListener(ApplicationReadyEvent.class)
    default void init() {
        Logger logger = LoggerFactory.getLogger(IFunc.class);
        setUp();
        logger.info("已加载：{}", this.getClass().getSimpleName());
    }


}
