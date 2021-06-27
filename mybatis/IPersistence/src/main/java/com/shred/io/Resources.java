package com.shred.io;

import java.io.InputStream;

public class Resources {
    /**
     * 根据配置文件庐江
     * @param path
     * @return
     */
    public static InputStream getResourceAsStream(String path){
        InputStream resourceAsStream = Resources.class.getClassLoader().getResourceAsStream(path);
        return resourceAsStream;
    }
}
