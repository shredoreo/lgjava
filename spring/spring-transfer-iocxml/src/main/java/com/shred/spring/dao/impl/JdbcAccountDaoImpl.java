package com.shred.spring.dao.impl;

import com.shred.spring.pojo.Account;
import com.shred.spring.dao.AccountDao;
import com.shred.spring.utils.ConnectionUtils;
import com.shred.spring.utils.DruidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author 应癫
 */
//@Repository("accountDao")
public class JdbcAccountDaoImpl implements AccountDao {

    //  @Autowired按照类型来注入
    @Autowired
    private ConnectionUtils connectionUtils;

    @Value("zzz")
    private String name;

    /*public JdbcAccountDaoImpl(ConnectionUtils connectionUtils, String name) {
        this.connectionUtils = connectionUtils;
        this.name = name;
    }
*/
    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }


    public void init() {
        System.out.println("初始化方法.....");
    }

    public void destory() {
        System.out.println("销毁方法......");
    }

    @Override
    public Account queryAccountByCardNo(String cardNo) throws Exception {
        //从连接池获取连接
//         Connection con = DruidUtils.getInstance().getConnection();
//        Connection con = connectionUtils.getCurrentThreadConn();
        Connection con = connectionUtils.getCurrentThreadConn();
        String sql = "select * from account where cardNo=?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1,cardNo);
        ResultSet resultSet = preparedStatement.executeQuery();

        Account account = new Account();
        while(resultSet.next()) {
            account.setCardNo(resultSet.getString("cardNo"));
            account.setName(resultSet.getString("name"));
            account.setMoney(resultSet.getInt("money"));
        }

        resultSet.close();
        preparedStatement.close();
        //con.close();

        return account;
    }

    @Override
    public int updateAccountByCardNo(Account account) throws Exception {

        // 从连接池获取连接
        // 改造为：从当前线程当中获取绑定的connection连接
//        Connection con = DruidUtils.getInstance().getConnection();
//        Connection con = connectionUtils.getCurrentThreadConn();
        Connection con = connectionUtils.getCurrentThreadConn();

        String sql = "update account set money=? where cardNo=?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setInt(1,account.getMoney());
        preparedStatement.setString(2,account.getCardNo());
        int i = preparedStatement.executeUpdate();

        preparedStatement.close();
        //con.close();
        return i;
    }

    public void setName(String name) {
        this.name = name;
    }
}
