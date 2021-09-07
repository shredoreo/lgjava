package com.shred.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EntityScan("com.shred.sc.pojo")
//@EnableEurekaClient // 开启EurekaClient
@EnableDiscoveryClient //开启注册中心客户端（通用注解，如注册到Eureka、Nacos等
    //说明：从 SpringCloud Edgware版本开始，可以不佳注解
public class ShredResumeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShredResumeApplication.class, args);
    }
}
