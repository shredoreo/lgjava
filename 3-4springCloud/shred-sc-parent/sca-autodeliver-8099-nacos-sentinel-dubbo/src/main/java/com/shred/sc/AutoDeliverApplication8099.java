package com.shred.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // 开启服务发现
public class AutoDeliverApplication8099 {

    public static void main(String[] args) {
        SpringApplication.run(AutoDeliverApplication8099.class, args);
    }

}
