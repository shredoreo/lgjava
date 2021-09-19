package com.shred.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EmailApp8082 {
    public static void main(String[] args) {
        SpringApplication.run(EmailApp8082.class, args);
    }
}
