package com.shred.spdemo;

import com.shred.spdemo.config.JdbcConfig;
import com.shred.spdemo.pojo.OwnerProperties;
import com.shred.spdemo.pojo.ThirdPartComponent;
import com.shred.spdemo.pojo.Person;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
class SpdemoApplicationTests {
    @Autowired
    private Person person;

    @Test
    void contextLoads() {
        System.out.println(person);
    }

    @Autowired
    private JdbcConfig jdbcConfig;
    @Autowired
    private DataSource dataSource;

    @Test
    public void test1(){
        System.out.println(jdbcConfig);
        System.out.println(dataSource);
    }

    @Autowired
    private ThirdPartComponent thirdPartComponent;

    @Test
    public void test2(){
        System.out.println("添加");
        System.out.println(thirdPartComponent);
    }

    @Autowired
    private OwnerProperties ownerProperties;
    @Test
    public void test3(){
        System.out.println(ownerProperties);
    }


    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testLog(){

        // 按级别升序
        logger.trace("trace ...");
        logger.debug("debug...");
        logger.info("info...");// spring boot 默认日志级别为info，也就是sb的root级别
        logger.warn("warn...");
        logger.error("error...");

    }

}
