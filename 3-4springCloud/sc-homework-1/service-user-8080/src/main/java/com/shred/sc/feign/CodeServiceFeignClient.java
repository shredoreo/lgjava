package com.shred.sc.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient
@RequestMapping("/code")
public interface CodeServiceFeignClient {
    @GetMapping("/validate/{email}/{code}")
    Integer verifyCode(@PathVariable("email") String email,
                       @PathVariable("code") String code);
}
