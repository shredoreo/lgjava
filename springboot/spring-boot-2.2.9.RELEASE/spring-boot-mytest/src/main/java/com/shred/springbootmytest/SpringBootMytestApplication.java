package com.shred.springbootmytest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class SpringBootMytestApplication/* extends SpringBootServletInitializer */{

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMytestApplication.class, args);
	}

	/*@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SpringBootMytestApplication.class);
	}*/
}
