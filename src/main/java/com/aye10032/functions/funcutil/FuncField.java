package com.aye10032.functions.funcutil;

import java.lang.annotation.*;

/**
 * 方法字段注解 只可用于Zibenbot这个主类中
 * 用这个注解的字段会在{@link FuncLoader}加载对象的时候进行写入
 *
 * @author Dazo66
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FuncField {
}
