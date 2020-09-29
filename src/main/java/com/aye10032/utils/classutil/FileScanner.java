package com.aye10032.utils.classutil;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Dazo66
 */
public class FileScanner implements IClassScanner {

    private String defaultClassPath = FileScanner.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    public FileScanner(String defaultClassPath) {
        this.defaultClassPath = defaultClassPath;
    }

    public FileScanner() {
    }

    public String getDefaultClassPath() {
        return defaultClassPath;
    }

    public void setDefaultClassPath(String defaultClassPath) {
        this.defaultClassPath = defaultClassPath;
    }

    @Override
    public Set<Class<?>> search(String packageName, Predicate<Class<?>> predicate) {
        //先把包名转换为路径,首先得到项目的classpath
        String classpath = defaultClassPath;
        //然后把我们的包名basPack转换为路径名
        String basePackPath = packageName.replace(".", File.separator);
        String searchPath = classpath + basePackPath;
        return new ClassSearcher().doPath(new File(searchPath), packageName, predicate, true);
    }

    private static class ClassSearcher {
        private Set<Class<?>> classPaths = new HashSet<>();

        private Set<Class<?>> doPath(File file, String packageName, Predicate<Class<?>> predicate, boolean flag) {

            if (file.isDirectory()) {
                //文件夹我们就递归
                File[] files = file.listFiles();
                if (!flag) {
                    packageName = packageName + "." + file.getName();
                }

                for (File f1 : files) {
                    doPath(f1, packageName, predicate, false);
                }
            } else {//标准文件
                //标准文件我们就判断是否是class文件
                if (file.getName().endsWith(CLASS_SUFFIX)) {
                    //如果是class文件我们就放入我们的集合中。
                    try {
                        Class<?> clazz = Class.forName(packageName + "." + file.getName().substring(0, file.getName().lastIndexOf(".")));
                        if (predicate == null || predicate.test(clazz)) {
                            classPaths.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new ScannerClassException(e.getMessage(), e);
                    }
                }
            }
            return classPaths;
        }
    }
}