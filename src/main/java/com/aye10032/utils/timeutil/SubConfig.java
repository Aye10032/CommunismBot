package com.aye10032.utils.timeutil;

import java.lang.annotation.*;

/**
 * 订阅器的配置类
 * 可以不放在订阅器上 存在默认配置
 *
 * @author Dazo66
 */

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SubConfig {

    /**
     * 配置当前订阅器是否支持用户参数
     * 默认为true 支持用户参数
     * 如果不支持 订阅时如果有传入参数 则会被舍弃
     * 则每次运行时传入的参数为null
     *
     * @return boolean
     */
    boolean noArgs();

}

