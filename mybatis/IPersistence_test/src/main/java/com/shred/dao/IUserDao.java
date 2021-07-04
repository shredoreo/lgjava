package com.shred.dao;

import com.shred.pojo.User;

import java.util.List;

public interface IUserDao {
    List<User> findAll();

    User findByCondition(User user);

    User findById(User user);

    int update(User user);

    int insert(User user);

    int delete(User user);
}
