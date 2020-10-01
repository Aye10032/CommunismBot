package com.aye10032.functions.funcutil;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ArrayUtils;
import com.aye10032.utils.ExceptionUtils;
import com.aye10032.utils.classutil.ClassUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 方法加载器
 * 默认加载{@link com.aye10032.functions}下的所有模块
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
public class FuncLoader {

    private List<String> scanPaths = new ArrayList<>();
    private List<IFuncFactory> factories = new ArrayList<>();
    private Zibenbot zibenbot;

    public FuncLoader(Zibenbot zibenbot) {
        this.zibenbot = zibenbot;
        //默认搜索路径
        addScanPackage("com.aye10032.functions");
    }

    private static Class<? extends IFuncFactory> getFactoryClass(Class<? extends IFunc> c) {
        FuncFactory annotation = c.getAnnotation(FuncFactory.class);
        return annotation == null ? null : annotation.value();
    }

    /**
     * 添加模块扫描的包名
     *
     * @param path 需要扫描的地方
     */
    public void addScanPackage(String path) {
        scanPaths.add(path);
    }

    /**
     * 添加IFunc的工厂对象 用于创建对象{@link IFuncFactory}
     *
     * @param funcFactory 工厂对象
     */
    public void addFactory(IFuncFactory funcFactory) {
        factories.add(funcFactory);
    }

    /**
     * 加载所有的模块
     * 在这个方法中会进行对象注入
     *
     * @return 模块的list
     */
    public List<IFunc> load() {
        List<IFunc> res = new ArrayList<>();
        Set<Class<? extends IFunc>> scanResult = scan();
        for (Class<? extends IFunc> c : scanResult) {
            if (c.getAnnotation(UnloadFunc.class) == null) {
                Class<? extends IFuncFactory> factoryClass = getFactoryClass(c);
                if (factoryClass != null) {
                    IFuncFactory factory = getFactory(factoryClass);
                    if (factory != null) {
                        res.add(factory.build());
                    } else {
                        zibenbot.logWarning("加载模块失败，错误原因：需要工厂类，找不到工厂类");
                    }
                } else {
                    try {
                        res.add(c.getConstructor(Zibenbot.class).newInstance(zibenbot));
                    } catch (NoSuchMethodException e) {
                        zibenbot.logWarning("加载模块失败，错误原因：找不到(Zibenbot zibenbot)这样的默认构造函数");
                    } catch (IllegalAccessException e) {
                        zibenbot.logWarning("加载模块失败，错误原因：参数不合法");
                    } catch (InstantiationException e) {
                        zibenbot.logWarning("加载模块失败，错误原因：类创建异常\n" + ExceptionUtils.printStack(e));
                    } catch (InvocationTargetException e) {
                        zibenbot.logWarning("加载模块失败，错误原因：调用目标异常");
                    }
                }
            }
        }
        inject(res);
        return res;
    }

    private void inject(List<IFunc> funcs) {
        Field[] fields = zibenbot.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(FuncField.class) != null) {
                IFunc func = ArrayUtils.findOne(funcs, iFunc -> field.getType() == iFunc.getClass());
                if (func != null) {
                    try {
                        field.set(zibenbot, func);
                    } catch (IllegalAccessException e) {
                        zibenbot.logWarning("注入模块失败，错误原因：参数异常");
                    }
                } else {
                    zibenbot.logWarning("注入模块失败，错误原因：找不到可以注入的对象");
                }
            }
        }
    }

    private IFuncFactory getFactory(Class<? extends IFuncFactory> c) {
        for (IFuncFactory factory : factories) {
            if (factory.getClass() == c) {
                return factory;
            }
        }
        return null;
    }

    private Set<Class<? extends IFunc>> scan() {
        Set<Class<? extends IFunc>> set = new HashSet<>();
        for (String path : scanPaths) {
            set.addAll(ClassUtil.getAllAssignedClass(IFunc.class, path));
        }
        return set;
    }

}
