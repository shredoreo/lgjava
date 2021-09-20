package com.shred.sc.controller;

import com.shred.sc.pojo.Token;
import com.shred.sc.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*",maxAge = 3600)
public class UserController {
    @Autowired
    private IUserService userService;


    @RequestMapping("/register/{email}/{password}/{code}")
    public int register(@PathVariable("email") String email,
                            @PathVariable("password") String password,
                            @PathVariable("code") String code){
        return userService.register(email, password, code);
    }

//    @RequestMapping("/login/{email}/{password}")
    public String login(@PathVariable("email") String email,
                        @PathVariable("password") String password
    ){

        try{
            Token token = userService.findToken(email, password);

            return token.getToken();
        }catch (Exception e){
            return "false";
        }

    }

    @RequestMapping("/login/{email}/{password}")
    public String login(@PathVariable("email") String email,
                         @PathVariable("password") String password,
                        HttpServletResponse response
                        ){

        try{
            Token token = userService.findToken(email, password);
            Cookie cookie = new Cookie("token", token.getToken());
            cookie.setDomain("test.com");
            cookie.setPath("/");
            cookie.setMaxAge(22222222);
            response.addCookie(cookie);
            return token.getToken();
        } catch (Exception e){
            return "false";
        }

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
