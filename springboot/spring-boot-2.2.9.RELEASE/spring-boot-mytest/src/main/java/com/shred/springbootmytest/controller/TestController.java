package com.shred.springbootmytest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@RequestMapping("/test")
	public String test(){
		System.out.println("源码环境构建成功！");
		return "源码环境构建成功！";
	}
}
