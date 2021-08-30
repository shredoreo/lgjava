package com.shred.service.impl;

import com.shred.service.HelloService;
import org.apache.dubbo.common.URL;

public class DogHelloService implements HelloService {
    @Override
    public String sayHello() {
        return "hello Doggy.";
    }

    @Override
    public String sayHello(URL url) {
        return "wang url";
    }
}
