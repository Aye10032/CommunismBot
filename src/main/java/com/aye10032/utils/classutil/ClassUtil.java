package com.aye10032.utils.classutil;


import com.aye10032.functions.funcutil.IFunc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class ClassUtil {


    public static void main(String[] args) {

        System.out.println("接口实现类：");
        for (Class<?> c : getAllAssignedClass(IFunc.class, getSuperPackagePath(IFunc.class.getPackage()))) {
            System.out.println(c.getName());
        }
        System.out.println("子类：");
        for (Class<?> c : getAllAssignedClass(IFunc.class, getSuperPackagePath(IFunc.class.getPackage()))) {
            System.out.println(c.getName());
        }

    }

    public static Set<Class<?>> searchClasses(String packageName) {
        return searchClasses(packageName, null);
    }

    public static Set<Class<?>> searchClasses(String packageName, Predicate predicate) {
        return ScanExcutor.getInstance().search(packageName, predicate);
    }


    public static String getSuperPackagePath(Package package1) {
        String name = package1.getName();
        return name.substring(0, name.lastIndexOf(org.apache.commons.lang3.ClassUtils.PACKAGE_SEPARATOR));
    }

    /**
     * 获取同一路径下所有子类或接口实现类
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static <T> Set<Class<? extends T>> getAllAssignedClass(Class<T> cls, String clsPath) {
        Set<Class<? extends T>> classes = new HashSet<>();
        for (Class<?> c : searchClasses(clsPath.replace('/', '.'))) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add((Class<? extends T>) c);
            }
        }
        return classes;
    }

    /**
     * 迭代查找类
     *
     * @param dir
     * @param pk
     * @return
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!dir.exists()) {
            return classes;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, pk + "." + f.getName()));
            }
            String name = f.getName();
            if (name.endsWith(".class")) {
                classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
            }
        }
        return classes;
    }


}
