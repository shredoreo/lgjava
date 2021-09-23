package com.shred.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.shred.sc.pojo")
public class OAuthApplication9999 {
    public static void main(String[] args) {
        SpringApplication.run(OAuthApplication9999.class, args);
    }
}
