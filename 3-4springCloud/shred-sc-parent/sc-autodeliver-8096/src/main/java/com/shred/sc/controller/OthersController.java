package com.shred.sc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/others")
public class OthersController {
    
    @RequestMapping("/test")
    public String test(){
        return "others/test";
    }
}