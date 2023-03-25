package com.aye10032.foundation.utils;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * the common methods of reflection
 *
 * @author shuang.kou
 * @createTime 2020年09月25日 14:23:00
 **/
@Slf4j
public class ReflectionUtils {

    /**
     * scan the classes marked by the specified annotation in the specified package
     *
     * @param packageNames specified package name
     * @param annotation   specified annotation
     * @return the classes marked by the specified annotation in the specified package
     */
    public static Set<Class<?>> scanAnnotatedClass(String[] packageNames, Class<? extends Annotation> annotation) {
        //获取到所有带此注解的类
//        使用 Reflections 可以查询以下元数据信息：
//        1）获得某个类型的所有子类型
//        2）获得标记了某个注解的所有类型／成员变量，支持注解参数匹配。
//        3）使用正则表达式获得所有匹配的资源文件
//        4）获得所有特定签名（包括参数，参数注解，返回值）的方法
//        new TypeAnnotationsScanner   确定扫描方式为扫描类的注解
        Reflections reflections = new Reflections(packageNames, new TypeAnnotationsScanner());
        //获取到所有带此注解类   annotation  注入的直接值
        Set<Class<?>> annotatedClass = reflections.getTypesAnnotatedWith(annotation, true);
        //打印日志
        log.info("The number of class Annotated with @" + annotation.getSimpleName() + ":[{}]", annotatedClass.size());
        //返回类
        return annotatedClass;
    }

    /**
     * 获取接口的实现类
     * Get the implementation class of the interface
     *
     * @param packageNames   specified package name
     * @param interfaceClass specified interface
     */
    public static <T> Set<Class<? extends T>> getSubClass(Class<T> interfaceClass, Object... packageNames) {
        Reflections reflections = new Reflections(packageNames);
        return reflections.getSubTypesOf(interfaceClass);

    }

    /**
     * 主要作用是实例化类 返回了对象
     * create object instance through class
     *
     * @param cls target class
     * @return object created by the target class
     */
    public static Object newInstance(Class<?> cls) {
        try {
            //springIOC和AOP是看见代码中很实用newInstance来实例化一个对象，之前对newInstance和new实例化对象的区别很模糊，特意在这里记录一下
            //
            //一、newInstance()和new()区别：
            //
            //　　1、两者创建对象的方式不同，前者是实用类的加载机制，后者则是直接创建一个类：
            //
            //　　2、newInstance创建类是这个类必须已经加载过且已经连接，new创建类是则不需要这个类加载过
            //
            //　　3、newInstance: 弱类型(GC是回收对象的限制条件很低，容易被回收)、低效率、只能调用无参构造，new 强类型
            // （GC不会自动回收，只有所有的指向对象的引用被移除是才会被回收，若对象生命周期已经结束，但引用没有被移除，经常会出现内存溢出）
            return cls.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * set the value of a field in the object  给类赋值
     *
     * @param obj   target object
     * @param field target field
     * @param value the value assigned to the field
     */
    public static void setField(Object obj, Field field, Object value) {
//        将此对象的 accessible 标志设置为指示的布尔值。值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查。
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException impossible) {
            throw new AssertionError(impossible);
        }

    }

    /**
     * execute the target method
     *
     * @param method target method
     * @param args   method parameters
     * @return the result of method execution
     */
    public static Object executeTargetMethod(Object targetObject, Method method, Object... args) {
        try {
            return method.invoke(targetObject, args);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * execute the void method
     *
     * @param method target method
     * @param args   method parameters
     */
    public static void executeTargetMethodNoResult(Object targetObject, Method method, Object... args) {
        try {
            // invoke target method through reflection
            method.invoke(targetObject, args);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }
}