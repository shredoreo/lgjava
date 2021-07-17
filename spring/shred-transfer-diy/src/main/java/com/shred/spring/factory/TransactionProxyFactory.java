package com.shred.spring.factory;

import com.shred.spring.anno.def.Autowired;
import com.shred.spring.anno.def.Component;
import com.shred.spring.anno.def.Transactional;
import com.shred.spring.utils.TransactionManager;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class TransactionProxyFactory {
    @Autowired
    private TransactionManager transactionManager;

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Object getJdkProxy(Object obj) {

        return Proxy.newProxyInstance(
                obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                (proxy, method, args1) -> {
                    Object result = null;
                    Method originMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
                    //类与方法上都没有标注Transactional
                    if (!checkMarkTransactional(obj, method)) {
                        return method.invoke(obj, args1);
                    }

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

    /**
     * 检查方法或类 是否标注Transactional
     * @param obj
     * @param method
     * @return
     * @throws NoSuchMethodException
     */
    public boolean checkMarkTransactional(Object obj, Method method) throws NoSuchMethodException {
        //不能使用代理对象的方法来判断
        //需使用原对象的原方法 才有注解的标注
        Method originMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
        //类或 方法上标注了 Transactional
        return originMethod.isAnnotationPresent(Transactional.class)
                || obj.getClass().isAnnotationPresent(Transactional.class);
    }

    public Object getCglibProxy(Object obj) {
        return Enhancer.create(
                obj.getClass(),
                new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                        Object result = null;

                        //类与方法上都没有标注@Transactional
                        if (!checkMarkTransactional(obj, method)) {
                            return method.invoke(obj, args);
                        }

                        try {
                            transactionManager.beginTransaction();
                            result = method.invoke(obj, args);

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
