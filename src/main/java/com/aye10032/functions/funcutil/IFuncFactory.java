package com.aye10032.functions.funcutil;

/**
 * IFunc模块的创建类
 * 需要如果模块需要Factory进行创建 需要对类添加{@link FuncFactory}注解
 * 并且把工厂类对象添加到{@link FuncLoader}的对象里去
 *
 * @author Dazo66
 */
public interface IFuncFactory<T extends IFunc> {

    /**
     * 构建对象
     *
     * @return 构建出来的对象
     */
    T build();

}
