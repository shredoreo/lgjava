package com.shred.ssm.mapper;

import com.shred.ssm.pojo.Account;

import java.util.List;

public interface AccountMapper {
    //定义dao层接口方法
    List<Account> queryAccountList();
}
