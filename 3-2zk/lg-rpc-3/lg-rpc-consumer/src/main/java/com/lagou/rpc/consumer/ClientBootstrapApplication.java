package com.lagou.rpc.consumer;

import com.lagou.rpc.consumer.discover.DiscoveryClient;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ClientBootstrapApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ClientBootstrapApplication.class, args);
    }

    @Autowired
    DiscoveryClient discoveryClient;

    @Override
    public void run(String... args) throws Exception {
        ZkClient zkClient = new ZkClient("tx1:2181");
        zkClient.subscribeChildChanges("/rpc", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> list) throws Exception {

            }
        })

        //启用服务发现
        discoveryClient.startClient();
    }
}
