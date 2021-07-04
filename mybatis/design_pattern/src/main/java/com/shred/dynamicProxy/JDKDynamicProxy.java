package com.shred.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKDynamicProxy implements InvocationHandler {

    private Person person;


    public JDKDynamicProxy(Person person) {
        this.person = person;
    }

    public Object getTarget(){
        Object o = Proxy.newProxyInstance(
                person.getClass().getClassLoader(),
                person.getClass().getInterfaces(),
                this
        );
        return o;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("前置增强");
        //执行原方法
        Object invoke = method.invoke(person, args);

        System.out.println("后置增强");

        return invoke;
    }
}
