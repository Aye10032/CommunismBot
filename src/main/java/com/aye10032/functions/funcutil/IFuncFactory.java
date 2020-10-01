package com.aye10032.functions.funcutil;

/**
 * IFunc模块的创建类
 * 需要如果模块需要Factory进行创建 需要对类添加{@link FuncFactory}注解
 * 并且把工厂类对象添加到{@link FuncLoader}的对象里去
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
public interface IFuncFactory<T extends IFunc> {

    /**
     * 构建对象
     *
     * @return 构建出来的对象
     */
    T build();

}
