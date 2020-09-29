package com.aye10032.functions.funcutil;

import java.lang.annotation.*;

/**
 * 不想加载的方法用这个注解 告诉加载器不加载
 *
 * @author Dazo66
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface UnloadFunc {
}
