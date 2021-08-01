package com.shred.sb_data_access;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan("com.shred.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//排除默认的ds配置，这样自定义的才能生效
public class SbDataAccessApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbDataAccessApplication.class, args);
	}

}
