package com.shred.spring.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 确保一个线程使用一个connection
 */
@Component("connectionUtils")
public class ConnectionUtils {

 /*   private ConnectionUtils(){

    }
    private static ConnectionUtils connectionUtils =new ConnectionUtils();

    public static ConnectionUtils getInstance(){
        return connectionUtils;
    }*/

    //保存当前线程的数据库连接
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    @Autowired
    @Qualifier("dataSource")
    private DataSource druidDataSource;

    public Connection getCurrentThreadConn() throws SQLException {
        Connection connection = threadLocal.get();
        if (connection == null) {
            connection = druidDataSource.getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }



}
