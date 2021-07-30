package com.shred.springbootmytest;

import com.shred.config.EnableRegisterServer;
import com.shred.pojo.SimpleBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableRegisterServer
public class StarterTest {
	@Autowired
	private SimpleBean simpleBean;

	@Test
	public void test(){
		System.out.println(simpleBean);
	}

}
