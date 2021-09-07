package com.shred.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.shred.sc.pojo")
public class ShredResumeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShredResumeApplication.class, args);
    }
}
