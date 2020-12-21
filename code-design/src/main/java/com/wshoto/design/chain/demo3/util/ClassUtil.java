package com.wshoto.design.chain.demo3.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {

    public static List<Class> getAllInterfaceAchieveClass(Class c){
        ArrayList<Class> list = new ArrayList<>();
        // 判断是否是接口
        if (c.isInterface()) {
            ArrayList<Class> allClass = getAllClassByPath(c.getPackage().getName());
        }
    }

    private static ArrayList<Class> getAllClassByPath(String packageName) {
        List<Class> list = new ArrayList<>();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(',', '/');


        List<File> files = new ArrayList<>();
        contextClassLoader.getResource(path);
    }
}
