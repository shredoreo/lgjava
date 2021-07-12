package com.shred.spring.dao.impl;

import com.shred.spring.dao.AccountDao;
import com.shred.spring.pojo.Account;
import com.shred.spring.utils.ConnectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository("accountDao")
public class JdbcTemplateDaoImpl implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("zzz")
    private String name;


    public void init() {
        System.out.println("初始化方法.....");
    }

    public void destory() {
        System.out.println("销毁方法......");
    }

    @Override
    public Account queryAccountByCardNo(String cardNo) throws Exception {

        String sql = "select * from account where cardNo=?";

        return jdbcTemplate.queryForObject(sql,new Object[]{cardNo}, new RowMapper<Account>() {
            @Override
            public Account mapRow(ResultSet resultSet, int i) throws SQLException {
                Account account = new Account();
                account.setName(resultSet.getString("name"));
                account.setCardNo(resultSet.getString("cardNo"));
                account.setMoney(resultSet.getInt("money"));
                return account;
            }
        });
    }

    @Override
    public int updateAccountByCardNo(Account account) throws Exception {
        String sql = "update account set money=? where cardNo=?";
        return jdbcTemplate.update(sql, account.getMoney(), account.getCardNo());
    }

    public void setName(String name) {
        this.name = name;
    }
}
