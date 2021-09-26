package com.shred.sc.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Feign 做的事：获取服务提供者的url，拼接路径，发起请求
 */
@FeignClient(value = "sc-resume",path = "/resume")
public interface ResumeServiceFeignClient {

    @GetMapping("/openstate/{userId}")
    public Integer findDefaultResumeState(@PathVariable("userId") Long userId);//PathVariable此处需要指定
}
