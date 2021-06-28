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

public class CacheTest {

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
    public void firstLevelCache(){
        User one = userMapper.findOne(1);

        one.setUsername("hhh");
        userMapper.update(one);
        sqlSession.commit();

        User user2 = userMapper.findOne(1);

        System.out.println(one == user2);

    }


    @Test
    public void SecondeLevelCache(){
        SqlSession sqlSession1 = sessionFactory.openSession();
        SqlSession sqlSession2= sessionFactory.openSession();
        SqlSession sqlSession3 = sessionFactory.openSession();

        IUserMapper userMapper1 = sqlSession1.getMapper(IUserMapper.class);
        IUserMapper userMapper2= sqlSession2.getMapper(IUserMapper.class);
        IUserMapper userMapper3 = sqlSession3.getMapper(IUserMapper.class);

        User user1 = userMapper1.findOne(1);
        //清空session1的缓存，会使二级缓存失效
//        sqlSession1.clearCache();
        //关闭session1，二级缓存存在
        sqlSession1.close();

        //并不发出sql
        User user2 = userMapper2.findOne(1);

        System.out.println(user1 == user2 );//false 二级缓存并不直接缓存对象，而是缓存对象的数据，第二次读取时创建一个新的对象

    }

}
