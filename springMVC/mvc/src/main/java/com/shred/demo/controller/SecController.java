package com.shred.demo.controller;

import com.shred.demo.service.IDemoService;
import com.shred.framework.mvc.annotations.Security;
import com.shred.framework.mvc.annotations.ShredAutowired;
import com.shred.framework.mvc.annotations.ShredController;
import com.shred.framework.mvc.annotations.ShredRequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Security(value = {"aa", "bb"})
@ShredController
@ShredRequestMapping("/sec")
public class SecController {
    @ShredAutowired
    private IDemoService demoService;

    @ShredRequestMapping("/handler01")
    public String handler01(HttpServletRequest request,
                            HttpServletResponse response,
                            String name) {
        return demoService.get(name);
    }

    @ShredRequestMapping("/handler02")
    @Security(value = {"cc"})
    public String handler02(HttpServletRequest request,
                            HttpServletResponse response,
                            String name) {
        return demoService.get(name);
    }


}
