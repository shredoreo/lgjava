package com.shred.sc.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/test")
    public String test(){

        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();

        return "demo/test";
    }
}
