package com.shred.ssm.controller;

import com.shred.ssm.pojo.Account;
import com.shred.ssm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {
    /**
     * Spring 容器和SpringMVC容器是有层次的
     * Spring 容器：Service对象和Dao对象
     * SpringMVC容器：Controller对象，可以引用到Spring容器中的对象
     */
    @Autowired
    private AccountService accountService;


    @RequestMapping("/queryAll")
    @ResponseBody
    public List<Account> queryAll(){
        return accountService.queryAccountList();

    }


}
