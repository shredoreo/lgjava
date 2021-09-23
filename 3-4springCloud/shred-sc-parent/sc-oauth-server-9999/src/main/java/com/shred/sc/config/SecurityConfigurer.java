package com.shred.sc.config;

import com.netflix.discovery.converters.Auto;
import com.shred.sc.service.JdbcUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

/**
 * 配置类，主要用于处理用户名 、密码校验
 */
@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcUserDetailsService jdbcUserDetailsService;

    /**
     * 注册一个认证管理器对象
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 密码编码对象，不进行加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return  NoOpPasswordEncoder.getInstance();
    }

    /**
     * 处理用户名 和 密码验证
     * 1、客户端提交username、password
     * 2、一般，username、password存在用户表
     * 3、根据表中的数据验证用户信息的合法性
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 在这个方法中即可关联数据库、当前我们先把用户信息配置在内存中。
        // 实例化一个用户对象
       /* UserDetails user = new User("admin", "123456", new ArrayList<>());
        auth.inMemoryAuthentication()
                .withUser(user)
                .passwordEncoder(passwordEncoder())
                ;*/
        auth.userDetailsService(jdbcUserDetailsService)
                .passwordEncoder(passwordEncoder);

    }
}
