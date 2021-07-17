package com.shred.spring.utils;

import com.shred.spring.anno.def.Autowired;
import com.shred.spring.anno.def.Component;

import java.sql.SQLException;

@Component
public class TransactionManager {

    @Autowired
    private ConnectionUtils connectionUtils;

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    /*   private TransactionManager(){}

    private static TransactionManager transactionManager = new TransactionManager();

    public static TransactionManager getInstance(){
        return transactionManager;
    }*/

    public void beginTransaction() throws SQLException {
        System.out.println("TransactionManager>>>>开启事务。。。");
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }

    public void commit() throws SQLException {
        System.out.println("TransactionManager>>>>提交事务。。。");
        connectionUtils.getCurrentThreadConn().commit();

    }

    public void rollback() throws SQLException {
        System.out.println("TransactionManager>>>>回滚事务。。。");
        connectionUtils.getCurrentThreadConn().rollback();
    }

}
