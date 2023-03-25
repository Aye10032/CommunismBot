package com.aye10032.foundation.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class ReflectUtils {

    private ReflectUtils() {
    }

    /**
     * 在给定的对象中获得指定字段的值，包括超类的，可以多层迭代查找
     *
     * @param o         指定的对象
     * @param fieldName 字段名称 可以进行多层迭代查找 使用[.]进行分割
     * @param type      最终返回的类型
     * @return 值
     * @throws Exception 反射过程中可能会出现的异常
     */
    public static <T> T getPrivateValueIncludeSuper(Object o, String fieldName, Class<T> type) throws Exception {
        String[] fieldNames = fieldName.split("\\.");
        for (String name : fieldNames) {
            System.out.println(Arrays.toString(getFieldIncludeSuper(o.getClass())));
            Field field = getFieldIncludeSuper(o.getClass(), name);
            field.setAccessible(true);
            o = field.get(o);
        }
        return (T) o;

    }

    public static Field getFieldIncludeSuper(Class<?> c, String name) {
        for (Field field : getFieldIncludeSuper(c)) {
            if (name.equals(field.getName())) {
                return field;
            }
        }
        throw new NoSuchElementException(name);
    }

    public static Field[] getFieldIncludeSuper(Class<?> c) {
        ArrayList<Field> list = new ArrayList<>();
        list.addAll(Arrays.asList(c.getDeclaredFields()));
        if (c.getSuperclass() != Object.class) {
            list.addAll(Arrays.asList(getFieldIncludeSuper(c.getSuperclass())));
        }
        return list.toArray(new Field[]{});
    }

}
