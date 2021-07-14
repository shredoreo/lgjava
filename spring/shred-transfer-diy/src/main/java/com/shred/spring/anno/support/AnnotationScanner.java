package com.shred.spring.anno.support;

import com.shred.spring.anno.def.Autowired;
import com.shred.spring.anno.def.Component;
import com.shred.spring.anno.def.Service;
import com.shred.spring.anno.def.Transactional;
import com.shred.spring.factory.BeanFactory;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class AnnotationScanner {
    List<Class> classList;
    Map<Class,List<Class>> annoListMap;
//    BeanFactory beanFactory;

    public AnnotationScanner(List<Class> classList) {
        this.classList = classList;

        this.annoListMap = new HashMap<>();

//        this.beanFactory = beanFactory;
    }

    public void doScan() throws Exception {
        scanClassAnnotations();

        instanceBean();

        scanFieldAnnotations();

        scanMethodAnnotations();
    }

    /**
     * 初始化扫描的bean，并放入beanFactory内
     */
    private void instanceBean() throws Exception {
        ArrayList<Class> beans = getAnnotatedBeanClasses();
        for (Class beanType : beans) {
            Object o = beanType.newInstance();
//            String beanName = beanType.getName();
//            beanName = beanName.substring(0,1).toUpperCase(Locale.ROOT).concat(beanName.substring(1));

//            BeanFactory.putBean(beanName,o);
            BeanFactory.putBean(beanType, o);

            resolveAutoWired(beanType, o);

        }

    }

    /**
     * 处理 @Autowired
     * @param beanType
     * @param o
     */
    private void resolveAutoWired(Class beanType, Object o) throws IllegalAccessException {
        Field[] declaredFields = beanType.getDeclaredFields();
        for (Field field : declaredFields) {
            Autowired annotation = field.getAnnotation(Autowired.class);
            if (annotation == null){
                continue;
            }
            String name = annotation.name();
            //指定名称，获取bean
            Object valOfField;
            if (StringUtils.isNotBlank(name)){
                valOfField = BeanFactory.getBean(name);
            } else {
                Class<?> fieldType = field.getType();
                valOfField = BeanFactory.getBeanByClass(fieldType);
            }
            field.set(o, valOfField);
        }
    }

    private void scanFieldAnnotations() {


    }

    /**
     * 扫描方法注解
     */
    private void scanMethodAnnotations() {
        ArrayList<Class> beans = getAnnotatedBeanClasses();

        scanSpecificFieldAnno(Autowired.class, beans);


    }

    private ArrayList<Class> getAnnotatedBeanClasses() {
        List<Class> compoents = this.annoListMap.get(Component.class);

        List<Class> services = this.annoListMap.get(Service.class);

        ArrayList<Class> beans = new ArrayList<>();
        if (compoents!=null){
            beans.addAll(compoents);
        }
        if (services!=null){
            beans.addAll(services);
        }
        return beans;
    }

    private void scanSpecificFieldAnno(Class<Autowired> autowiredClass, ArrayList<Class> beans) {
        for (Class bean : beans) {
            Field[] declaredFields = bean.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Autowired annotation = declaredField.getAnnotation(autowiredClass);
                if (annotation!=null){
                    String name = annotation.name();

                    if (StringUtils.isNotBlank(name)){

                    }
                }
            }
        }
    }

    /**
     * 扫描类上注解
     */
    public void scanClassAnnotations(){
        //用在类上到注解
        scanSpecificAnno(Component.class);
        scanSpecificAnno(Service.class);
        scanSpecificAnno(Transactional.class);

    }

    private void scanSpecificAnno(Class<?> componentClass) {
        for (Class aClass : classList) {
            Annotation annotation = aClass.getAnnotation(componentClass);
            if (annotation == null){
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
