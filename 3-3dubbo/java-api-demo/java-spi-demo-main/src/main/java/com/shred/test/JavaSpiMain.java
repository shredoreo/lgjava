package com.shred.test;

import com.shred.service.HelloService;

import java.util.ServiceLoader;

public class JavaSpiMain {
    public static void main(String[] args) {
        // 通过spi机制加载类
        // 可以看到以及自动实例化了
        ServiceLoader<HelloService> helloServices = ServiceLoader.load(HelloService.class);
        for (HelloService helloService : helloServices) {
            System.out.println(helloService.getClass().getName() +": " +helloService.sayHello());
        }
    }
}
