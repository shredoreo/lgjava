package com.shred.spring.service.impl;

import com.shred.spring.anno.def.Autowired;
import com.shred.spring.anno.def.Service;
import com.shred.spring.anno.def.Transactional;
import com.shred.spring.dao.AccountDao;
import com.shred.spring.factory.BeanFactory;
import com.shred.spring.pojo.Account;
import com.shred.spring.service.TransferService;
import com.shred.spring.utils.TransactionManager;

@Service(name = "AAAtransferServiceAAA")
public class TransferServiceImpl implements TransferService {

    @Autowired
    private AccountDao accountDao;

    // 构造函数传值/set方法传值

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    @Transactional
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

            Account from = accountDao.queryAccountByCardNo(fromCardNo);
            Account to = accountDao.queryAccountByCardNo(toCardNo);

            from.setMoney(from.getMoney()-money);
            to.setMoney(to.getMoney()+money);

            accountDao.updateAccountByCardNo(to);
            int c = 1/0;
            accountDao.updateAccountByCardNo(from);

    }
}
