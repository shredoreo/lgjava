package com.shred.springbootmytest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@Value("${com.name}")
	private String name;

	@Value("${com.location}")
	private String location;

	@RequestMapping("hello")
	@ResponseBody
	public String profile1(){
		String x = name + " hello s b " + location;
		System.out.println(x);
		return x;
	}

}
