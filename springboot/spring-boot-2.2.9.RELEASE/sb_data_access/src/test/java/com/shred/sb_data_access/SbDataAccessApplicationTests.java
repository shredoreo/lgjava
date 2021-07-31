package com.shred.sb_data_access;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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

}
