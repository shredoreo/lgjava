package com.shred.spring.pojo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 拦截实例化之后的对象（实例化并且属性已注入）
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("lazyResult".equalsIgnoreCase(beanName)){
            System.out.println("postProcessBeforeInitialization lazyResult: "+ bean);
        }
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("lazyResult".equalsIgnoreCase(beanName)){
            System.out.println("postProcessAfterInitialization lazyResult: "+ bean);
        }
        return null;
    }
}
