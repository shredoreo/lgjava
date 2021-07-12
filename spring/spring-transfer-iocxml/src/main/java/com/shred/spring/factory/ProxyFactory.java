package com.shred.spring.factory;

import com.shred.spring.utils.TransactionManager;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//@Component("proxyFactory")
public class ProxyFactory {
    private TransactionManager transactionManager;

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
/*

    private ProxyFactory() {
    }

    private static ProxyFactory factory = new ProxyFactory();

    public static ProxyFactory getInstance() {
        return factory;
    }
*/

    public Object getJdkProxy(Object obj) {

        return Proxy.newProxyInstance(
                obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                (proxy, method, args1) -> {
                    Object result = null;
                    try {
                        transactionManager.beginTransaction();
                        result = method.invoke(obj, args1);
                        transactionManager.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                        transactionManager.rollback();
                        throw e;
                    }
                    return result;
                }
        );

    }

    public Object getCglibProxy(Object obj) {
        return Enhancer.create(
                obj.getClass(),
                new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        Object result = null;
                        try {
                            transactionManager.beginTransaction();
                            result = method.invoke(obj, objects);

                            transactionManager.commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                            transactionManager.rollback();
                            throw e;
                        }

                        return result;
                    }
                }
        );
    }

}
