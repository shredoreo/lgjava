package com.shred.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableHystrixDashboard
@SpringBootApplication
@EnableDiscoveryClient
public class HystrixDashBoardApplication9000 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashBoardApplication9000.class, args);
    }
}
