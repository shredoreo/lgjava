package com.shred.sc.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "service-user")
@RequestMapping("/user")
public interface UserFeignClient {

    @RequestMapping("/info/{token}")
    String getInfo(@PathVariable(name = "token") String token);

    @RequestMapping("/login/{email}/{password}")
    public String login(@PathVariable("email") String email,
                        @PathVariable("password") String password);
}
