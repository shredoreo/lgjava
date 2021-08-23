package com.lagou.rpc.consumer;

import com.lagou.rpc.consumer.discover.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientBootstrapApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ClientBootstrapApplication.class, args);
    }

    @Autowired
    DiscoveryClient discoveryClient;

    @Override
    public void run(String... args) throws Exception {
        //启用服务发现
        discoveryClient.startClient();
    }
}
