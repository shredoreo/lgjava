package com.shred.service.impl;

import com.shred.service.HelloService;

public class HelloServiceDog implements HelloService {
    @Override
    public String sayHello() {
//        System.out.println("hello service");
        return "hello service doggy";
    }
}
