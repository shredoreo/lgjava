package com.shred;

import com.shred.bean.ConsumerComponent;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.*;

import java.io.IOException;

public class ConsumerMain {

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        ctx.start();
        ConsumerComponent consumerComponent = ctx.getBean(ConsumerComponent.class);
        while (true){
            //输入字符触发
            System.in.read();
            try {
                String shred = consumerComponent.sayHello("shred");
                System.out.println("结果："+ shred);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Configuration
    @EnableDubbo
    @PropertySource("classpath:/dubbo-consumer.properties")
    @ComponentScan(value = {"com.shred.bean"})
    static class ConsumerConfiguration{


    }
}
