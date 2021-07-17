package com.shred.spring.anno.support;

import java.util.List;

public class AnnotationSupport {

    public void support(String packageName) throws Exception {
        //类扫描
        ClassScanner classScanner = new ClassScanner();
        List<Class<?>> classes = classScanner.doScan(packageName);

        AnnotationScanner annotationScanner = new AnnotationScanner(classes);
        //扫描注解
        annotationScanner.scanAndInject();
    }
}
