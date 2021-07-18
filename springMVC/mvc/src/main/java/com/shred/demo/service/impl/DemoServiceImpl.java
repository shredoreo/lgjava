package com.shred.demo.service.impl;

import com.shred.demo.service.IDemoService;
import com.shred.framework.mvc.annotations.ShredService;

@ShredService
public class DemoServiceImpl implements IDemoService {

    @Override
    public String get(String name) {
        System.out.println("serivcwe 获取的name " + name);
        return name;
    }
}
