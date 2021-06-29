package com.shred.proxy;

import org.junit.Test;

import java.lang.reflect.Proxy;

public class DynamicProxyTest {
    @Test
    public void test(){
        //被代理对象
        Learning me = new People();

        //创建代理对象
        Learning proxyInstance =(Learning) Proxy.newProxyInstance(
                me.getClass().getClassLoader(),
                People.class.getInterfaces(),
                new LearningInvocationHandler(me));

        ///执行代理方法
        proxyInstance.learn();

    }
}
