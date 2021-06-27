package com.shred.test;

import com.shred.mapper.IOrderMapper;
import com.shred.mapper.IUserMapper;
import com.shred.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class CacheTest {

    private IUserMapper userMapper;
    IOrderMapper orderMapper;
    SqlSession sqlSession;



    @Before
    public void before() throws Exception{
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        sqlSession = new SqlSessionFactoryBuilder().build(resourceAsStream).openSession(false);
        userMapper = sqlSession.getMapper(IUserMapper.class);
        orderMapper = sqlSession.getMapper(IOrderMapper.class);
    }

    @Test
    public void firstLevelCache(){
        User one = userMapper.findOne(1);

        one.setUsername("hhh");
        userMapper.update(one);
        sqlSession.commit();

        User user2 = userMapper.findOne(1);

        System.out.println(one == user2);

    }

}
