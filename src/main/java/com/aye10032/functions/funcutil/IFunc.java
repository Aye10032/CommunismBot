package com.aye10032.functions.funcutil;

/**
 * @author Dazo66
 */
public interface IFunc {


    /**
     * 初始化方法
     * 由bot主体在startUp()中调用
     */
    void setUp();

    /**
     * 执行模块
     * @param simpleMsg
     */
    void run(SimpleMsg simpleMsg);


}
