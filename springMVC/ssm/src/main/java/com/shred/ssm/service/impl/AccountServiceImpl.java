package com.shred.ssm.service.impl;

import com.shred.ssm.mapper.AccountMapper;
import com.shred.ssm.pojo.Account;
import com.shred.ssm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<Account> queryAccountList() {
        return accountMapper.queryAccountList();
    }
}
