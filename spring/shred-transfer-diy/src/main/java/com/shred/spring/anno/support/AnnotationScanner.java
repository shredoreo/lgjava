package com.shred.spring.anno.support;

import com.shred.spring.anno.def.Autowired;
import com.shred.spring.anno.def.Component;
import com.shred.spring.anno.def.Service;
import com.shred.spring.anno.def.Transactional;
import com.shred.spring.exception.AmbiguousBeanException;
import com.shred.spring.exception.BeanNoDefException;
import com.shred.spring.factory.BeanFactory;
import com.shred.spring.factory.ProxyFactory;
import com.shred.spring.service.impl.TransferServiceImpl;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class AnnotationScanner {
    /**
     * 待扫描的类
     */
    List<Class> classList;
    /**
     * 注解扫描 key：注解；  val：类集合
     */
    Map<Class<?>, Set<Class<?>>> annoListMap;
    private HashSet<Class<?>> beanRegistrySet;

    public AnnotationScanner(List<Class> classList) {
        this.classList = classList;

        this.annoListMap = new HashMap<>();

//        this.beanFactory = beanFactory;
    }

    public void doScan() throws Exception {
        //扫描带注解的类
        scanClassAnnotations();

        //实例化化Bean
        instanceBean();

        //扫描字段注解
//        scanFieldAnnotations();

        //扫描方法注解
//        scanMethodAnnotations();
    }

    /**
     * 初始化扫描的bean，并放入beanFactory内
     */
    private void instanceBean() throws Exception {
        HashSet<Class<?>> beans = getAnnotatedBeanClasses();
        this.beanRegistrySet = beans;

        for (Class<?> beanType : beans) {
            //先从容器中获取（因为在autowire过程中会实例化依赖，并直接放入）
            String name;
            if (beanType.isAnnotationPresent(Component.class)) {
                name = beanType.getAnnotation(Component.class).name();
            } else {
                name = beanType.getAnnotation(Service.class).name();
            }

            Object bean = BeanFactory.getBean(beanType);

            //获取不到再创建。并放入容器
            if (bean == null) {
                bean = beanType.newInstance();
                BeanFactory.putBean(beanType, bean);
            }

            resolveClassAnnos(beanType, bean);
            resolveFieldAnnos(beanType, bean);
            resolveMethodAnnos(beanType, bean);

        }

    }

    private void resolveClassAnnos(Class<?> beanType, Object bean) {
        resolveTransactional(beanType, bean);
    }

    private void resolveFieldAnnos(Class<?> beanType, Object bean) throws BeanNoDefException, AmbiguousBeanException, IllegalAccessException, InstantiationException {
        resolveAutoWired(beanType, bean);
    }

    private void resolveMethodAnnos(Class<?> beanType, Object bean) {
        resolveMethodTransactional(beanType, bean);
    }


    private void resolveTransactional(Class<?> beanType, Object bean) {
        if (beanType.isAnnotationPresent(Transactional.class)) {
            //可能用到注解的一些信息
            Transactional annotation = beanType.getAnnotation(Transactional.class);


        }
    }

    /**
     * 解析方法上的 Transactional
     *
     * @param beanType
     * @param bean
     * @return
     */
    private Object resolveMethodTransactional(Class beanType, Object bean) {
        Class[] interfaces = beanType.getInterfaces();
        ProxyFactory proxyFactory = BeanFactory.getBean(ProxyFactory.class);
        Object proxy;

        if (interfaces.length == 0) {
            proxy = proxyFactory.getCglibProxy(bean);
        } else {
            proxy = proxyFactory.getJdkProxy(bean);
        }

        return proxy;
    }

    /**
     * 处理 @Autowired
     * 未解决循环依赖
     *
     * @param beanType
     * @param o
     */
    private void resolveAutoWired(Class<?> beanType, Object o) throws IllegalAccessException, BeanNoDefException, InstantiationException, AmbiguousBeanException {
        Field[] declaredFields = beanType.getDeclaredFields();
        for (Field field : declaredFields) {
            Autowired annotation = field.getAnnotation(Autowired.class);
            if (annotation == null) {
                continue;
            }
            String name = annotation.name();
            //指定名称，获取bean
            Object valOfField;
            Class<?> fieldType = field.getType();

            if (BeanFactory.checkExist(fieldType)) {

                if (StringUtils.isNotBlank(name)) {
                    valOfField = BeanFactory.getBean(name);
                } else {
                    valOfField = BeanFactory.findBeanByType(fieldType);
                }

            } else {
                throw new BeanNoDefException("beanDefNotFound:" + fieldType.getName());
            }

            //未初始化，立即初始化，并放入容器
            if (valOfField == null) {
                valOfField = fieldType.newInstance();
                BeanFactory.putBean(fieldType, valOfField);
            }

            field.setAccessible(true);
            field.set(o, valOfField);
        }
    }

    private void scanFieldAnnotations() {


    }

    private HashSet<Class<?>> getAnnotatedBeanClasses() {
        Set<Class<?>> compoents = this.annoListMap.get(Component.class);

        Set<Class<?>> services = this.annoListMap.get(Service.class);

        HashSet<Class<?>> beans = new HashSet<>();
        if (compoents != null) {
            beans.addAll(compoents);
        }
        if (services != null) {
            beans.addAll(services);
        }
        return beans;
    }


    /**
     * 扫描类上注解
     */
    public void scanClassAnnotations() {
        //用在类上到注解
        scanSpecificAnno(Component.class);
        scanSpecificAnno(Service.class);
        scanSpecificAnno(Transactional.class);

    }

    private void scanSpecificAnno(Class<?> componentClass) {
        for (Class aClass : classList) {
            Annotation annotation = aClass.getAnnotation(componentClass);
            if (annotation == null) {
                continue;
            }
            //保存到Map中
            this.annoListMap.computeIfAbsent(componentClass,
                    key -> new HashSet<>()
            ).add(aClass);

        }
    }

    public static void main(String[] args) throws Exception {
        ClassScanner classScanner = new ClassScanner();
        List<Class> classes = classScanner.doScan("com.shred.spring");
        AnnotationScanner annotationScanner = new AnnotationScanner(classes);

        //扫描注解
        annotationScanner.doScan();

        Object bean = BeanFactory.getBean(TransferServiceImpl.class);
        System.out.println(bean);
    }
}
