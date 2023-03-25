package com.aye10032.foundation.utils.classutil;

import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Dazo66
 */
public class ScanExcutor implements IClassScanner {

    private volatile static ScanExcutor instance;

    private ScanExcutor() {
    }

    public static ScanExcutor getInstance() {
        if (instance == null) {
            synchronized (ScanExcutor.class) {
                if (instance == null) {
                    instance = new ScanExcutor();
                }
            }
        }
        return instance;
    }

    @Override
    public Set<Class<?>> search(String packageName, Predicate<Class<?>> predicate) {
        IClassScanner fileSc = new FileScanner();
        Set<Class<?>> fileSearch = fileSc.search(packageName, predicate);
        IClassScanner jarScanner = new JarClassScanner();
        Set<Class<?>> jarSearch = jarScanner.search(packageName, predicate);
        fileSearch.addAll(jarSearch);
        return fileSearch;
    }

}