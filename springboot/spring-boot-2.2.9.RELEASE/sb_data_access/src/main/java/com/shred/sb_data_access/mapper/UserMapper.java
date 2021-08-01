package com.shred.sb_data_access.mapper;

import com.shred.sb_data_access.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

	@Select("SELECT * FROM USER")
	public List<User> getUser();
}
