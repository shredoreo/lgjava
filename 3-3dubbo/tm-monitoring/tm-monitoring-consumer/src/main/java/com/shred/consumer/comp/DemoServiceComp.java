package com.shred.consumer.comp;

import com.shred.service.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
@Component("demoServiceComponent")
public class DemoServiceComp implements DemoService{
    @Reference
    private DemoService demoService;

    @Override
    public String methodA() throws Exception {
        String s = demoService.methodA();
        System.out.println(s);
        return s;
    }

    @Override
    public String methodB() throws Exception {
        String s = demoService.methodB();
        System.out.println(s);
        return s;
    }

    @Override
    public String methodC() throws Exception {
        String s = demoService.methodC();
        System.out.println(s);
        return s;
    }
}
