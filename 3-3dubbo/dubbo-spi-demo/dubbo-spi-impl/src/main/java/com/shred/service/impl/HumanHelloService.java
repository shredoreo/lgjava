package com.shred.service.impl;

import com.shred.service.HelloService;
import org.apache.dubbo.common.URL;

public class HumanHelloService implements HelloService {
    @Override
    public String sayHello() {
        return "hello human";
    }

    @Override
    public String sayHello(URL url) {
        return "human url";
    }
}
