package com.shred.spdemo.config;


import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration//注入容器
@EnableConfigurationProperties(JdbcConfig.class)//使下面的注解生效
@ConfigurationProperties(prefix = "jdbc")
@Data
public class JdbcConfig {

//    @Value("${jdbc.driver}")
    private String driverClassName;

//    @Value("${jdbc.url}")
    private String url;

//    @Value("${jdbc.username}")
    private String username;

//    @Value("${jdbc.password}")
    private String password;

    @Bean
    public DataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);

        return druidDataSource;
    }

}
