package com.shred.sc.controller;

import com.shred.sc.service.IMailService;
import com.shred.sc.service.impl.MailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private IMailService mailService;


    @GetMapping("/{email}/{code}")
    public Boolean sendCode(@PathVariable String email,
                            @PathVariable String code){

        return mailService.sendMail(email, "网站 注册验证码", code);
    }

}
