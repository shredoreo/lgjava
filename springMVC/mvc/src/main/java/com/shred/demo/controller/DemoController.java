package com.shred.demo.controller;

import com.shred.demo.service.IDemoService;
import com.shred.framework.mvc.annotations.ShredAutowired;
import com.shred.framework.mvc.annotations.ShredController;
import com.shred.framework.mvc.annotations.ShredRequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ShredController
@ShredRequestMapping("/demo")
public class DemoController {

    @ShredAutowired
    private IDemoService demoService;

    //url：http://localhost:8080/demo/query?name=11&name=1232a
    @ShredRequestMapping("/query")
    public String query(HttpServletRequest request,
                        HttpServletResponse response,
                        String name){
        return demoService.get(name);
    }

}
