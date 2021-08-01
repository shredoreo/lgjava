package com.shred.sb_data_access;

import com.shred.sb_data_access.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@MapperScan("com.shred.mapper")
@RunWith(SpringRunner.class)
@SpringBootTest
class SbDataAccessApplicationTests {
	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() throws SQLException {
		Connection connection = dataSource.getConnection();
		System.out.println(connection);
	}

	@Autowired
	private UserService userService;

	@Test
	public void test(){
		System.out.println(userService.findAllUser());
	}
}
