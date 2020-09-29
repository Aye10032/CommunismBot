package com.aye10032.functions.funcutil;

import java.lang.annotation.*;

/**
 * 如果需要自定义创建IFunc 请在子类中添加这个注解
 * 否则一律以 (Zibenbot zibenbot) 的构造函数进行创建
 *
 * @author Dazo66
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FuncFactory {

    Class<? extends IFuncFactory> value();

}
