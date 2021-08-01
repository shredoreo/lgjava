package com.shred.sb_data_access.service;

import com.shred.sb_data_access.mapper.UserMapper;
import com.shred.sb_data_access.pojo.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	public List<User> findAllUser(){
		List<User> user = userMapper.getUser();
		System.out.println(user);
		return user;
	}
}
