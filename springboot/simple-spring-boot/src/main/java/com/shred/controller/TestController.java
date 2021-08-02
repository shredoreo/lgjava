package com.shred.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/app")
public class TestController {

    @RequestMapping("/test")
    public String test(){
        return "hello";
    }

}
