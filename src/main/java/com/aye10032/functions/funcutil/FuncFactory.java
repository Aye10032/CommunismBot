package com.aye10032.functions.funcutil;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FuncFactory {

    Class<? extends IFuncFactory> value();

}
