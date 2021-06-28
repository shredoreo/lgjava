package com.shred.test;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shred.mapper.IOrderMapper;
import com.shred.mapper.IUserMapper;
import com.shred.mapper.UserMapper;
import com.shred.pojo.Order;
import com.shred.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisTest {

    @Test
    public void test1t() throws Exception{
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSession sqlSession = new SqlSessionFactoryBuilder().build(resourceAsStream).openSession();
        IOrderMapper userMapper = sqlSession.getMapper(IOrderMapper.class);

        List<Order> orderAndUser = userMapper.findOrderAndUser();

        System.out.println(orderAndUser);

    }



    @Test
    public void test2() throws Exception{
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSession sqlSession = new SqlSessionFactoryBuilder().build(resourceAsStream).openSession();
        IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);

        List<User> all = userMapper.findAll();

        all.forEach(System.out::println);

        sqlSession.close();
    }

    @Test
    public void test3() throws Exception{
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSession sqlSession = new SqlSessionFactoryBuilder().build(resourceAsStream).openSession();
        IUserMapper userMapper = sqlSession.getMapper(IUserMapper.class);

        List<User> all = userMapper.findAllUserAndRole();

        all.forEach(System.out::println);

        sqlSession.close();
    }

    private IUserMapper userMapper;
    IOrderMapper orderMapper;

    @Before
    public void before() throws Exception{
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSession sqlSession = new SqlSessionFactoryBuilder().build(resourceAsStream).openSession(true);
        userMapper = sqlSession.getMapper(IUserMapper.class);
        orderMapper = sqlSession.getMapper(IOrderMapper.class);
    }

    @Test
    public void test5(){
        User user = new User();
        user.setId(6);
        user.setUsername("王路飞");
        userMapper.add(user);


    }

    @Test
    public void test6(){
        User user1 = userMapper.findOne(1);
        System.out.println(user1);

        user1.setUsername("山贼王");

        userMapper.update(user1);

        userMapper.deleteUser(6);

    }


    @Test
    public void testOneToOne(){
        orderMapper.findOrderAndUser().forEach(System.out::println);
    }



    @Test
    public void testMTM(){
        userMapper.findAllUserAndRole().forEach(System.out::println);
    }

        @Test
        public void testPluginPageHelper() {
            PageHelper.startPage(1, 1);
            List<Order> orderAndUser = orderMapper.findOrderAndUser();
            orderAndUser.forEach(System.out::println);
            PageInfo<Order> userPageInfo = new PageInfo<>(orderAndUser);
            System.out.println(userPageInfo);

        }


    @Test
    public void testGenMapper() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSession sqlSession = new SqlSessionFactoryBuilder().build(resourceAsStream).openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setId(1);
        User user1 = mapper.selectOne(user);
        System.out.println(user1);


        //使用example查询
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("id", 1);
        List<User> users = mapper.selectByExample(example);
        users.forEach(System.out::println);

    }

}
