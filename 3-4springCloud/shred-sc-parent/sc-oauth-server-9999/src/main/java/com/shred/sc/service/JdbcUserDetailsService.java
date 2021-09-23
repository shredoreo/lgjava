package com.shred.sc.service;

import com.shred.sc.dao.UserRepository;
import com.shred.sc.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JdbcUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    /**
     * 获取数据库中的user，返回UserDetails对象
     * 用于com.shred.sc.config.SecurityConfigurer#configure
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查找user
        Users byUsername = userRepository.findByUsername(username);

        User user = new User(username, byUsername.getPassword(), new ArrayList<>());
        return user;
    }
}
