package com.aye10032.functions.funcutil;

/**
 * IFUNC模块的创建类
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
