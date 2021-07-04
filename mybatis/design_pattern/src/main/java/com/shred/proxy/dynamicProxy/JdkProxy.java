package com.shred.proxy.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxy {
    public static void main(String[] args) {
        //委托方
        IRentingHouse rentingHouse = new RentingHouseImpl();

        IRentingHouse proxy = (IRentingHouse) ProxyFactory.getInstance().getJdkProxy(rentingHouse);
        proxy.rentHouse();
    }
}
