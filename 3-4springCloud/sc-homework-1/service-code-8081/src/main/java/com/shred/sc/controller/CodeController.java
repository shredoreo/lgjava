package com.shred.sc.controller;

import com.shred.sc.feign.EmailServiceFeignClient;
import com.shred.sc.pojo.AuthCode;
import com.shred.sc.service.impl.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j(topic = "service-code")
@RequestMapping("/code")
public class CodeController {

    @Autowired
    private CodeService codeService;

    @Autowired
    private EmailServiceFeignClient emailFeign;



    @GetMapping("/create/{email}")
    public boolean createAndSend(@PathVariable String email){
       log.warn("创建验证码并发送"+email);
        //创建验证码
        AuthCode authCode = codeService.create(email);

        //发送邮件
        return emailFeign.sendCode(email, authCode.getCode());
    }


    @GetMapping("/validate/{email}/{code}")
    public Integer verifyCode(@PathVariable String email,
                              @PathVariable String code){
        log.warn("校验验证码"+email+"-->"+code);
        return codeService.validate(email, code);
    }
}
