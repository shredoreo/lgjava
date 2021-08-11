package com.shred.minicat.server.loader;

import java.io.*;

public class WebAppClassLoader extends ClassLoader {

    private String classpath;//classpath
    private final String  filetype=".class"; //文件类型
    public WebAppClassLoader(String classpath) {
        this.classpath = classpath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] data = loadData(name);

            if (data!=null){
                Class<?> aClass = defineClass(name, data, 0, data.length);
                return aClass;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private byte[] loadData(String name) throws Exception {

        name=name.replace('.', '/');
        File file = new File(classpath + name+ filetype);

        if (!file.exists()){
            return null;
        }

        try (
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ) {
            byte[] bytes = new byte[1024];
            int size = 0;
            while ((size = fis.read(bytes))!=-1){
                bos.write(bytes,0, size);
            }
            return bos.toByteArray();
        }

    }
}
