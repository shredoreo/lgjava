package com.shred.sc;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableDubbo
@SpringBootApplication
@EnableDiscoveryClient
//@EnableFeignClients// 开启feign客户端
public class CodeApp8081 {
    public static void main(String[] args) {
        SpringApplication.run(CodeApp8081.class, args);
    }
}
