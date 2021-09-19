package com.shred.sc.controller;

import com.shred.sc.dao.TokenDao;
import com.shred.sc.feign.CodeServiceFeignClient;
import com.shred.sc.pojo.Token;
import com.shred.sc.service.IUserService;
import com.shred.sc.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;


    @RequestMapping("/register/{email}/{password}/{code}")
    public void register(@PathVariable("email") String email,
                         @PathVariable("password") String password,
                         @PathVariable("code") String code){
        userService.register(email, password, code);
    }

    @RequestMapping("/login/{email}/{password}")
    public String login(@PathVariable("email") String email,
                         @PathVariable("password") String password){
       return userService.login(email, password);
    }


    @RequestMapping("/isRegistered/{email}")
    public boolean isRegistered(@PathVariable String email){
        return userService.isRegisterd(email);
    }

    @RequestMapping("/info/{token}")
    public String getInfo(@PathVariable String token){
        return userService.getInfoByToken(token);
    }

}
