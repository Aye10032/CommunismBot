package com.aye10032.functions.funcutil;

import java.lang.annotation.*;

/**
 * 方法字段注解 只可用于Zibenbot这个主类中
 * 用这个注解的字段会在{@link FuncLoader}加载对象的时候进行写入
 *
 * @see FuncFactory  工厂注解类 用于声明这个方法所需要的工厂
 * @see FuncField    字段注解类 用于声明这个字段需要自动注入对象
 *                   应用范围仅限于Zibenbot
 * @see UnloadFunc   方法类注解类 声明了之后告诉加载器不要加载这个类
 * @see IFuncFactory 工厂接口类 如果需要工厂进行对象创建 可以继承这个类
 * @see IFunc        方法接口类
 * @see BaseFunc     抽限方法类 推荐继承这个来创建新的方法
 * @see FuncLoader   方法加载类 用于加载IFunc
 * @author Dazo66
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FuncField {
}
