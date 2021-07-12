package com.shred.spring;

import com.alibaba.druid.pool.DruidDataSource;
import com.shred.spring.factory.CompanyFactoryBean;
import com.shred.spring.pojo.Result;
import com.shred.spring.utils.TransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"com.shred.spring"})
@PropertySources({@PropertySource("classpath:jdbc.properties")})
@EnableAspectJAutoProxy/*开启aop注解*/
@EnableTransactionManagement/*开启声明式事务注解*/
//@Import()//关联多个配置类
public class SpringConfig {

    @Value("${jdbc.driver}")
    private String driverClassName;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;


    @Bean("dataSource")
    public DataSource createDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(username);
        druidDataSource.setUrl(url);
        druidDataSource.setPassword(password);
        druidDataSource.setDriverClassName(driverClassName);
        return druidDataSource;
    }

    @Bean(initMethod = "initMethod")
    @Lazy
//    @Lazy(value = false)
    public Result lazyResult(){
        return new Result();

    }

    @Bean
    public CompanyFactoryBean companyBean(){
        CompanyFactoryBean companyFactoryBean = new CompanyFactoryBean();
        companyFactoryBean.setCompanyInfo("nnnn,ssfd,ff");
        return companyFactoryBean;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(createDataSource());
        return jdbcTemplate;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(){
        return new DataSourceTransactionManager(createDataSource());
    }
}

