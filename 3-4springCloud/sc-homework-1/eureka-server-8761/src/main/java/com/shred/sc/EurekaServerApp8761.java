package com.shred.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp8761 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApp8761.class, args);
    }
}
