package com.aye10032.functions.funcutil;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ArrayUtils;
import com.aye10032.utils.ExceptionUtils;
import com.aye10032.utils.classutil.ClassUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 方法加载器
 * 默认加载{@link com.aye10032.functions}下的所有模块
 *
 * @author Dazo66
 */
public class FuncLoader {

    private List<String> scanPaths = new ArrayList<>();
    private List<IFuncFactory> factories = new ArrayList<>();
    private Zibenbot zibenbot;

    public FuncLoader(Zibenbot zibenbot) {
        this.zibenbot = zibenbot;
    }

    private static Class<? extends IFuncFactory> getFactoryClass(Class<? extends IFunc> c) {
        FuncFactory annotation = c.getAnnotation(FuncFactory.class);
        return annotation == null ? null : annotation.value();
    }

    /**
     * 添加模块扫描的地址
     *
     * @param path 需要扫描的地方
     */
    public void addScanPath(String path) {
        scanPaths.add(path);
    }

    /**
     * 添加IFunc的工厂对象 用于创建对象
     *
     * @param funcFactory 工厂对象
     */
    public void addFactory(IFuncFactory funcFactory) {
        factories.add(funcFactory);
    }

    public List<IFunc> load() {
        List<IFunc> res = new ArrayList<>();
        Set<Class<? extends IFunc>> scanResult = scan();
        for (Class<? extends IFunc> c : scanResult) {
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
                    if (c != BaseFunc.class) {
                        res.add(c.getConstructor(Zibenbot.class).newInstance(zibenbot));
                    }
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
        Set<Class<? extends IFunc>> set = ClassUtil.getAllAssignedClass(IFunc.class, ClassUtil.getSuperPackagePath(IFunc.class.getPackage()));
        for (String path : scanPaths) {
            set.addAll(ClassUtil.getAllAssignedClass(IFunc.class, path));
        }
        return set;

    }

}
