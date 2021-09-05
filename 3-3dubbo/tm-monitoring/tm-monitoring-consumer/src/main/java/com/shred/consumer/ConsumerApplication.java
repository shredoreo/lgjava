package com.shred.consumer;

import com.shred.consumer.comp.DemoServiceComp;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConsumerApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        DemoServiceComp serviceProxy = context.getBean("demoServiceComponent",DemoServiceComp.class);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 100, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));
        long startTime = System.currentTimeMillis();
        int minutes = 3;
        int count = 0;
        while (true) {
            try {
                // 这里适当的休息一会，否则线程池不够用，会拒绝服务
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                threadPoolExecutor.execute(() -> {
                    try {
                        serviceProxy.methodA();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                threadPoolExecutor.execute(() -> {
                    try {
                        serviceProxy.methodB();
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                });
                threadPoolExecutor.execute(() -> {
                    try {
                        serviceProxy.methodC();
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            count++;
            // 执行三分钟结束
            if ((System.currentTimeMillis() - startTime) > minutes * 60 * 1000) break;
        }
        System.out.println("平均每分钟调用 - " + count / minutes + " - 次");


    }


    @Configuration
    @EnableDubbo(scanBasePackages = "com.shred.consumer.comp")
    @PropertySource("classpath:/spring/dubbo-consumer.properties")
    @ComponentScan(value = {"com.shred.consumer.comp"})
    static class ConsumerConfiguration {

    }
}
