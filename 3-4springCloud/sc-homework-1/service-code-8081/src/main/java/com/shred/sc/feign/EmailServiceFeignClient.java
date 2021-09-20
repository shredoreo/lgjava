package com.shred.sc.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "service-email")
@RequestMapping("/email")
public interface EmailServiceFeignClient {
    @GetMapping("/{email}/{code}")
    Boolean sendCode(@PathVariable("email") String email,
                            @PathVariable("code") String code);

}
