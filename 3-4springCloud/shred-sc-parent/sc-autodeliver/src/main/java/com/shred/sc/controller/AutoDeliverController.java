package com.shred.sc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/autodeliver")
public class AutoDeliverController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/checkStatu/{userId}")
    public Integer findResumeOpenState(@PathVariable Long userId){
        List<ServiceInstance> instances = discoveryClient.getInstances("sc-resume");



        ServiceInstance serviceInstance = instances.get(0);
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();

        //调用远程服务 - 简历微服务
        String url = "http://"+host+":"+port+"/resume/openstate/" + userId;
        System.out.println("从Eureka集群 获取服务实例 url："+url);
        Integer openStatus = restTemplate.getForObject(url, Integer.class);
        return openStatus;
    }
}
