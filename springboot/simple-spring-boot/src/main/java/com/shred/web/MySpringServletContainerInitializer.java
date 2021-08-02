package com.shred.web;

import com.shred.servlet.MyWebApplicationInitializer;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class MySpringServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        MyWebApplicationInitializer myWebApplicationInitializer = new MyWebApplicationInitializer();
        myWebApplicationInitializer.onStartup(servletContext);

    }
}
