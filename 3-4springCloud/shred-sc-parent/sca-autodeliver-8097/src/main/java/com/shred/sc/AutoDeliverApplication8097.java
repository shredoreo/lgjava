package com.shred.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient // 开启服务发现
@EnableFeignClients// 开启feign客户端
public class AutoDeliverApplication8097 {

    public static void main(String[] args) {
        SpringApplication.run(AutoDeliverApplication8097.class, args);
    }

}
