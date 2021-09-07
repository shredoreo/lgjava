package com.shred.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer//声明当前项目为eureka服务
public class EurekaServerApp8762 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApp8762.class, args);
    }
}
