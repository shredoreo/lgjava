package com.shred.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@MapperScan(basePackages = "com.shred.cache.mappers")
@SpringBootApplication
public class SbCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbCacheApplication.class, args);
	}

}
