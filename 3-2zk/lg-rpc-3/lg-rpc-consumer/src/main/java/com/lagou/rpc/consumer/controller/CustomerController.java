package com.lagou.rpc.consumer.controller;

import com.lagou.rpc.api.IUserService;
import com.lagou.rpc.consumer.proxy.RpcClientProxy;
import com.lagou.rpc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/consume")
public class CustomerController {

    @Autowired
    RpcClientProxy proxy;

    @GetMapping("/user/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id")int id){
        IUserService proxy = (IUserService) this.proxy.createProxy(IUserService.class);

        return proxy.getById(id);
    }

}
