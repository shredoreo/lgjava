package com.shred.spring.anno.support;

import com.shred.spring.anno.def.Autowired;
import com.shred.spring.anno.def.Component;
import com.shred.spring.anno.def.Service;
import com.shred.spring.anno.def.Transactional;
import com.shred.spring.exception.AmbiguousBeanException;
import com.shred.spring.exception.BeanNoDefException;
import com.shred.spring.factory.BeanFactory;
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
    Map<Class<?>, List<Class<?>>> annoListMap;
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
            Object bean = BeanFactory.getBean(beanType);
            //获取不到再创建。并放入容器
            if (bean == null) {
                bean = beanType.newInstance();
                BeanFactory.putBean(beanType, bean);
            }

            resolveAutoWired(beanType, bean);

            resolveMethodTransactional(beanType, bean);

        }

    }

    /**
     * 解析方法上的 Transactional
     *
     * @param beanType
     * @param bean
     */
    private void resolveMethodTransactional(Class beanType, Object bean) {

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

    /**
     * 扫描方法注解
     */
    private void scanMethodAnnotations() {
        HashSet<Class<?>> beans = getAnnotatedBeanClasses();

        scanSpecificFieldAnno(Autowired.class, beans);

    }

    private HashSet<Class<?>> getAnnotatedBeanClasses() {
        List<Class<?>> compoents = this.annoListMap.get(Component.class);

        List<Class<?>> services = this.annoListMap.get(Service.class);

        HashSet<Class<?>> beans = new HashSet<>();
        if (compoents != null) {
            beans.addAll(compoents);
        }
        if (services != null) {
            beans.addAll(services);
        }
        return beans;
    }

    private void scanSpecificFieldAnno(Class<Autowired> autowiredClass, HashSet<Class<?>> beans) {
        for (Class bean : beans) {
            Field[] declaredFields = bean.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Autowired annotation = declaredField.getAnnotation(autowiredClass);
                if (annotation != null) {
                    String name = annotation.name();

                    if (StringUtils.isNotBlank(name)) {

                    }
                }
            }
        }
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
                    key -> new ArrayList<>())
                    .add(aClass);

        }
    }

    public static void main(String[] args) throws Exception {
        ClassScanner classScanner = new ClassScanner();
        List<Class> classes = classScanner.doScan("com.shred.spring");
        AnnotationScanner annotationScanner = new AnnotationScanner(classes);

        //扫描注解
        annotationScanner.doScan();

    }
}
