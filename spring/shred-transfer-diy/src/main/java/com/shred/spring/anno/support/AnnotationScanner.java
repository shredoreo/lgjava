package com.shred.spring.anno.support;

import com.shred.spring.anno.def.Autowired;
import com.shred.spring.anno.def.Component;
import com.shred.spring.anno.def.Service;
import com.shred.spring.exception.AmbiguousBeanException;
import com.shred.spring.exception.BeanNoDefException;
import com.shred.spring.factory.BeanFactory;
import com.shred.spring.factory.TransactionProxyFactory;
import com.shred.spring.service.TransferService;
import org.apache.commons.lang.StringUtils;

import java.beans.IntrospectionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AnnotationScanner {
    /**
     * 待扫描的类
     */
    List<Class<?>> classList;
    /**
     * 注解扫描 key：注解；  val：类集合
     */
    Map<Class<?>, Set<Class<?>>> annoListMap;
    private HashSet<Class<?>> beanRegistrySet;

    public AnnotationScanner(List<Class<?>> classList) {
        this.classList = classList;

        this.annoListMap = new HashMap<>();

    }

    public void scanAndInject() throws Exception {
        System.out.println("开始扫描类");
        //扫描带注解的类
        scanComponents();

        System.out.println("开始扫描注解");
        //支持事务，提前注入事务代理工厂
        transactionSupport();

        System.out.println("开始注入类");
        //实例化化Bean
        instanceBean();

    }

    /**
     * 注入事务代理工厂
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void transactionSupport() throws IllegalAccessException, InstantiationException {
        BeanFactory.registerType(TransactionProxyFactory.class);
        TransactionProxyFactory transactionProxyFactory = TransactionProxyFactory.class.newInstance();
        BeanFactory.putBean(TransactionProxyFactory.class, null, transactionProxyFactory);
    }

    /**
     * 初始化扫描的bean，并放入beanFactory内
     */
    private void instanceBean() throws Exception {
        HashSet<Class<?>> beans = getAnnotatedBeanClasses();

        this.beanRegistrySet = beans;

        for (Class<?> beanType : beans) {
            //类型注册
            BeanFactory.registerType(beanType);

            String name;
            if (beanType.isAnnotationPresent(Component.class)) {
                name = beanType.getAnnotation(Component.class).name();
            } else {
                name = beanType.getAnnotation(Service.class).name();
            }

            //实例化
            Object bean = beanType.newInstance();

            //放入容器
            BeanFactory.putBean(beanType, name, bean);
        }

        //处理字段注解
        for (Class<?> beanType : beans) {

            //先从容器中获取（因为在autowire过程中会实例化依赖，并直接放入）
            String name;
            if (beanType.isAnnotationPresent(Component.class)) {
                name = beanType.getAnnotation(Component.class).name();
            } else {
                name = beanType.getAnnotation(Service.class).name();
            }

            Object bean = BeanFactory.getBean(beanType);
            // 处理字段注解：@Autowired
            resolveFieldAnnos(beanType, bean);

            //事务代理对象（在代理方法中处理 @Transactional ）
            bean = resolveTransactional(beanType, bean);

            //放入容器
            BeanFactory.putBean(beanType, name, bean);
        }

    }

    private void resolveClassAnnos(Class<?> beanType, Object bean) {
//        resolveTransactional(beanType, bean);
    }

    private void resolveFieldAnnos(Class<?> beanType, Object bean) throws Exception {
        // @Autowired
        resolveAutoWired(beanType, bean);
    }

    private void resolveMethodAnnos(Class<?> beanType, Object bean) {
    }


    /*private void resolveTransactional(Class<?> beanType, Object bean) {
        // 标注了Transactional
        if (beanType.isAnnotationPresent(Transactional.class)) {
            //可能用到注解的一些信息
            Transactional annotation = beanType.getAnnotation(Transactional.class);




        }
    }*/

    /**
     * 解析方法上的 Transactional
     *
     * @param beanType
     * @param bean
     * @return
     */
    private Object resolveTransactional(Class beanType, Object bean) throws BeanNoDefException, AmbiguousBeanException {
        Class[] interfaces = beanType.getInterfaces();
        TransactionProxyFactory transactionProxyFactory = (TransactionProxyFactory) BeanFactory.findBeanByType(TransactionProxyFactory.class);
        Object proxy;

        if (interfaces.length == 0) {
            //无接口，使用cglib
            proxy = transactionProxyFactory.getCglibProxy(bean);
        } else {
            //实现接口，使用jdk
            proxy = transactionProxyFactory.getJdkProxy(bean);
        }

        return proxy;
    }

    /**
     * 处理 @Autowired
     * 未解决循环依赖
     *
     * @param beanType
     * @param bean
     */
    private void resolveAutoWired(Class<?> beanType, Object bean) throws IllegalAccessException, BeanNoDefException, InstantiationException, AmbiguousBeanException, InvocationTargetException, IntrospectionException {
        Field[] declaredFields = beanType.getDeclaredFields();
        for (Field field : declaredFields) {
            Autowired annotation = field.getAnnotation(Autowired.class);
            if (annotation == null) {
                continue;
            }
            //指定了beanId
            String name = annotation.name();

            //指定名称，获取bean
            Object valOfField;
            Class<?> fieldType = field.getType();

            //是否注册过该类型的bean
            if (!BeanFactory.checkExist(fieldType)) {
                throw new BeanNoDefException("beanDefNotFound:" + fieldType.getName());
            }

            // 获取已初始化的bean
            if (StringUtils.isNotBlank(name)) {
                valOfField = BeanFactory.getBean(name);
            } else {
                valOfField = BeanFactory.findBeanByType(fieldType);
            }

            //方法设值
            field.setAccessible(true);
            field.set(bean, valOfField);

          /*
             String propertyName = field.getName();
             //报错，获取不到isXXX而不是getXXX
            PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, beanType);
            descriptor.getWriteMethod().invoke(bean, valOfField);
            */
            /*
                        Method[] methods = beanType.getMethods();

            for (int j = 0; j < methods.length; j++) {
                Method method = methods[j];

                //找到对应的setter方法
                if (method.getName().equalsIgnoreCase("set" + fieldType.getSimpleName())) {
                    method.invoke(bean, valOfField);
                }
            }*/
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
    public void scanComponents() {
        //用在类上到注解
        scanSpecificAnno(Component.class);
        scanSpecificAnno(Service.class);
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
/*

        ClassScanner classScanner = new ClassScanner();
        List<Class<?>> classes = classScanner.doScan("com.shred.spring");
        AnnotationScanner annotationScanner = new AnnotationScanner(classes);

        //扫描注解
        annotationScanner.scanAndInject();

        TransferService bean = (TransferService) BeanFactory.findBeanByType(TransferService.class);
        System.out.println(bean);

        bean.transfer("6029621011001", "6029621011000", 301);
*/

    }
}
