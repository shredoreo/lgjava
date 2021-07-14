package com.shred.spring.anno.support;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassScanner {
    List<String> classPaths = new ArrayList<>();

    /**
     * 扫描包 的方法
     *
     * @param packageName
     * @return
     */
    public List<Class> doScan(String packageName) throws ClassNotFoundException {
        String rootPath = ClassScanner.class.getResource("/").getPath();
        String subPath = packageName.replace(".", File.separator);
        String scanPath = rootPath + subPath;

        scanClass(new File(scanPath));
        ArrayList<Class> classes = new ArrayList<>();
        for (String path : classPaths) {
            //得到类的全限定路径
//            path = path.replace(rootPath.replace(rootPath,"\\").replaceFirst("\\\\",""),"").replace("\\",".").replace(".class","");
            path = path.replace(rootPath, "").replace("/", ".").replace(".class", "");
            try {

                Class<?> aClass = Class.forName(path);
                classes.add(aClass);
            } catch (NoClassDefFoundError e) {
//                e.printStackTrace();
                System.out.println("ERROR" + e.getMessage());
                continue;
            }
        }

        return classes;
    }

    private void scanClass(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                scanClass(f);
            }
        }
        //文件
        else {
            if (file.getName().endsWith(".class")) {
                //搜集class文件的路径
                classPaths.add(file.getPath());
            }
        }

    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> aClass = Class.forName("com.shred.spring.dao.AccountDao");
        System.out.println(aClass);
        List<Class> classes = new ClassScanner().doScan("com.shred.spring");
        System.out.println(classes);
    }


}
