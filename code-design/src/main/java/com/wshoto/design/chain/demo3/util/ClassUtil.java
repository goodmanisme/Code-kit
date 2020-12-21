package com.wshoto.design.chain.demo3.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author AGoodMan
 */
public class ClassUtil {

    /**
     * 获取所有接口的实现类
     *
     * @param c class
     * @return
     */
    public static List<Class> getAllInterfaceAchieveClass(Class c) {
        ArrayList<Class> list = new ArrayList<>();
        // 判断是否是接口
        if (c.isInterface()) {
            try {
                List<Class> classList = getAllClassByPath(c.getPackage().getName());
                for (int i = 0; i < classList.size(); i++) {
                    if (Modifier.isAbstract(classList.get(i).getModifiers())) {
                        continue;
                    }
                    if (c.isAssignableFrom(classList.get(i))) {
                        if (!c.equals(classList.get(i))) {
                            list.add(classList.get(i));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 从指定路径下获取所有类
     *
     * @param packageName 包路径
     * @return
     */
    private static List<Class> getAllClassByPath(String packageName) {
        List<Class> list = new ArrayList<>();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        try {
            List<File> files = new ArrayList<>();
            Enumeration<URL> enumeration = contextClassLoader.getResources(path);
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                files.add(new File(url.getFile()));
            }
            for (int i = 0; i < files.size(); i++) {
                list.addAll(findClass(files.get(i), packageName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 如果file是文件夹，则递归调用findClass方法，或文件夹下的类
     * 如果file本身是类文件，则加入list中进行保存，并返回
     *
     * @param file
     * @param packageName
     * @return
     */
    private static List<Class> findClass(File file, String packageName) {
        List<Class> list = new ArrayList<>();
        if (!file.exists()) {
            return list;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                assert !f.getName().contains(".");
                List<Class> classList = findClass(f, packageName + "." + f.getName());
                list.addAll(classList);
            } else if (f.getName().endsWith(".class")) {
                try {
                    list.add(Class.forName(packageName + "." + f.getName().substring(0, f.getName().length() - 6)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}