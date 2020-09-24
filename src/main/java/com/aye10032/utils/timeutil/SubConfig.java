package com.aye10032.utils.timeutil;

import java.lang.annotation.*;

/**
 * 指定订阅器是否支持参数
 * 推荐放在run方法上
 * 如果放在类体上 要注意创建匿名内部类的时候会抹去
 *
 * @author Dazo66
 */

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SubConfig {

    boolean noArgs();

}

