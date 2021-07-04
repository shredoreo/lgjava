package com.shred.test;

import com.shred.mapper.IOrderMapper;
import com.shred.mapper.IUserMapper;
import com.shred.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class LazyLoadTest {
    private IUserMapper userMapper;
    IOrderMapper orderMapper;
    SqlSession sqlSession;

    SqlSessionFactory sessionFactory;
    @Before
    public void before() throws Exception{
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        sqlSession = sessionFactory.openSession(false);
        userMapper = sqlSession.getMapper(IUserMapper.class);
        orderMapper = sqlSession.getMapper(IOrderMapper.class);
    }

    @Test
    public void test1(){
        User user = userMapper.findOne(1);
        System.out.println(user.getUsername());

        System.out.println(user.getOrderList());
    }
}
