package com.shred.spring.service.impl;

import com.shred.spring.dao.AccountDao;
import com.shred.spring.pojo.Account;
import com.shred.spring.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("transferService")
@Transactional
public class TransferServiceImpl implements TransferService {
    //  @Autowired 按照类型来注入, 若无发区分，可使用 @Qualifier
    @Autowired
    @Qualifier("accountDao")
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
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
