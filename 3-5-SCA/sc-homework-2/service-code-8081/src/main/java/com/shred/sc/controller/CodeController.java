package com.shred.sc.controller;

import com.shred.sc.CodeService;
import com.shred.sc.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j(topic = "service-code")
@RequestMapping("/code")
public class CodeController {

    @Autowired
    private CodeService codeService;

//    @Autowired
//    private EmailServiceFeignClient emailFeign;

    @Reference
    private EmailService emailService;


    @GetMapping("/create/{email}")
    public boolean createAndSend(@PathVariable String email){
       log.warn("创建验证码并发送"+email);
        //创建验证码
        String code = codeService.create(email);

        //发送邮件
        return emailService.sendCode(email,"发送验证码", code);
    }


    @GetMapping("/validate/{email}/{code}")
    public Integer verifyCode(@PathVariable String email,
                              @PathVariable String code){
        log.warn("校验验证码"+email+"-->"+code);
        return codeService.verifyCode(email, code);
    }
}
