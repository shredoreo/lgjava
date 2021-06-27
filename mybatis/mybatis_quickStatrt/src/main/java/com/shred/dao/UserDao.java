package com.shred.dao;

import com.shred.pojo.User;

import java.io.IOException;
import java.util.List;

public interface UserDao {
    List<User> findAll() throws IOException;

    User findByCondition(User user);

    List<User> findByIds(int[] ids);
}
