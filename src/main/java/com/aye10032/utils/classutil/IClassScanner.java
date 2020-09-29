package com.aye10032.utils.classutil;

import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Dazo66
 */
public interface IClassScanner {

    String CLASS_SUFFIX = ".class";

    Set<Class<?>> search(String packageName, Predicate<Class<?>> predicate);

    default Set<Class<?>> search(String packageName) {
        return search(packageName, null);
    }

}