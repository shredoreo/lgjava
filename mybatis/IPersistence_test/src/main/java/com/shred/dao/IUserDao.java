package com.shred.dao;

import com.shred.pojo.User;

import java.util.List;

public interface IUserDao {
    List<User> findAll();

    User findByCondition(User user);
}
