package com.shred.provider.service;

import com.shred.service.DemoService;
import org.apache.dubbo.config.annotation.Service;

import java.util.Random;

@Service
public class DemoServiceImpl implements DemoService {
    private Random random = new Random();

    private long getRandom() {
        return random.nextInt(100);
    }

    @Override
    public String methodA() throws Exception {
        Thread.sleep(getRandom());
        return "AAA method A invoked";
    }

    @Override
    public String methodB() throws Exception {
        Thread.sleep(getRandom());
        return "BBB method BB invoked";
    }

    @Override
    public String methodC() throws Exception {
        Thread.sleep(getRandom());
        return "CCC method CC invoked";
    }
}
