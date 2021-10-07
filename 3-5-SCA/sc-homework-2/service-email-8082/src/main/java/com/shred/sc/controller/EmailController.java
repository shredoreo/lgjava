package com.shred.sc.controller;

import com.shred.sc.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService mailService;


    @GetMapping("/{email}/{code}")
    public Boolean sendCode(@PathVariable String email,
                            @PathVariable String code){
        System.out.println("mail-service 发送验证码"+email+"-->"+code);
        return mailService.sendCode(email, "网站 注册验证码", code);
    }

}
