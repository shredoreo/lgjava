package com.shred.service;

import org.apache.dubbo.common.extension.ExtensionLoader;

public class HelloService$Adaptive implements com.shred.service.HelloService {
    public java.lang.String sayHello() {
        throw new UnsupportedOperationException("The method public abstract java.lang.String com.shred.service.HelloService.sayHello() of interface com.shred.service.HelloService is not adaptive method!");
    }

    public java.lang.String sayHello(org.apache.dubbo.common.URL arg0) {
        if (arg0 == null) throw new IllegalArgumentException("url == null");
        org.apache.dubbo.common.URL url = arg0;
        String extName = url.getParameter("hello.service", "dog");
        if (extName == null)
            throw new IllegalStateException("Failed to get extension (com.shred.service.HelloService) name from url (" + url.toString() + ") use keys([hello.service])");
        com.shred.service.HelloService extension = (com.shred.service.HelloService) ExtensionLoader.getExtensionLoader(com.shred.service.HelloService.class).getExtension(extName);
        return extension.sayHello(arg0);
    }
}