package com.shred.spring.factory;

import com.shred.spring.utils.ConnectionUtils;

public class CreateBeanFactory {

    public static ConnectionUtils getInstanceStatic(){
        return new ConnectionUtils();
    }

    public ConnectionUtils getInstance(){
        return new ConnectionUtils();
    }
}
