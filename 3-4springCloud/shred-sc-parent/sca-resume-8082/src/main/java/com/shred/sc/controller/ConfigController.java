package com.shred.sc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Value("${shred.message}")
    private String world;
    @Value("${abc.test}")
    private String abc;
    @Value("${def.test}")
    private String def;

    @GetMapping("/view")
    public String view(){
        System.out.println(world);
        return "hello.world=>"+world +" abc.test=>" + abc +" def.test=>"+def;
    }
}
