package com.shred.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LearningInvocationHandler implements InvocationHandler {

    private final Learning learning;

    /**
     * 通过构造器存储被代理对象
     * @param learning
     */
    public LearningInvocationHandler(Learning learning) {
        this.learning = learning;
    }

    /**
     * 代理方法
     * @param proxy 代理对象，newProxyInstance方法的返回对象
     * @param method 调用的方法
     * @param args 方法中的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("before: play one hour");
        Object invoke = method.invoke(learning, args);
        System.out.println("after: play one hour");

        return invoke;
    }
}
