package com.shred.singletom;

public class LazySingleton {
    private static LazySingleton instance;

    private LazySingleton(){}

    //静态方法返回实例，加synchronized实现线程同步
    public static synchronized LazySingleton getInstance(){
        if (instance == null){
            instance = new LazySingleton();
        }
        return instance;
    }

}
