package com.shred.proxy.dynamicProxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {

    private ProxyFactory(){}

    private static ProxyFactory factory = new ProxyFactory();

    public static ProxyFactory getInstance(){
        return factory;
    }

    public Object getJdkProxy(Object obj){

        return Proxy.newProxyInstance(
                obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                (proxy, method, args1) -> {
                    Object result = null;
                    System.out.println("中介收取2000");
                    result = method.invoke(obj, args1);
                    System.out.println("客户信息卖2毛");

                    return result;
                }
        );

    }

    public Object getCglibProxy(Object obj){
        return Enhancer.create(
                obj.getClass(),
                new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        Object result = null;
                        System.out.println("中介收取服务费2000");

                        result = method.invoke(obj, objects);

                        System.out.println("客户信息卖3毛钱");

                        return result;
                    }
                }
        );
    }
}
