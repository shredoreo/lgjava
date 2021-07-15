package com.shred.spring.service.impl;

import com.shred.spring.anno.def.Autowired;
import com.shred.spring.anno.def.Service;
import com.shred.spring.dao.AccountDao;
import com.shred.spring.factory.BeanFactory;
import com.shred.spring.pojo.Account;
import com.shred.spring.service.TransferService;
import com.shred.spring.utils.TransactionManager;

/**
 * @author 应癫
 */
@Service
public class TransferServiceImpl implements TransferService {

    //private AccountDao accountDao = new JdbcAccountDaoImpl();

    // private AccountDao accountDao = (AccountDao) BeanFactory.getBean("accountDao");

    // 最佳状态
    @Autowired
    private AccountDao accountDao;

    // 构造函数传值/set方法传值

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }



    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

//        try{
//            // 开启事务(关闭事务的自动提交)
//            TransactionManager.getInstance().beginTransaction();

            Account from = accountDao.queryAccountByCardNo(fromCardNo);
            Account to = accountDao.queryAccountByCardNo(toCardNo);

            from.setMoney(from.getMoney()-money);
            to.setMoney(to.getMoney()+money);

            accountDao.updateAccountByCardNo(to);
//            int c = 1/0;
            accountDao.updateAccountByCardNo(from);

            // 提交事务
//
//            TransactionManager.getInstance().commit();
//        }catch (Exception e) {
//            e.printStackTrace();
//            // 回滚事务
//            TransactionManager.getInstance().rollback();
//
//            // 抛出异常便于上层servlet捕获
//            throw e;
//
//        }

    }
}
