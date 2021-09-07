package com.shred.sc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/autodeliver")
public class AutoDeliverController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/checkStatu/{userId}")
    public Integer findResumeOpenState(@PathVariable Long userId){
        //调用远程服务 - 简历微服务
        Integer openStatus = restTemplate.getForObject("http://localhost:8080/resume/openstate/" + userId,
                Integer.class);
        return openStatus;
    }
}
