package com.shred.sc.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Feign 做的事：获取服务提供者的url，拼接路径，发起请求
 */
//@FeignClient表明当前类是Feign客户端
// value指定该客户端要请求的服务名称，（登记到注册中心上，服务提供者的服务名）
@FeignClient(value = "sc-resume")
@RequestMapping("/resume")
public interface ResumeServiceFeignClient {

    @GetMapping("/openstate/{userId}")
    public Integer findDefaultResumeState(@PathVariable("userId") Long userId);//PathVariable此处需要指定
}
