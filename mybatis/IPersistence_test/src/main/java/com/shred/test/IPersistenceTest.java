package com.shred.test;

import com.shred.dao.IUserDao;
import com.shred.io.Resources;
import com.shred.pojo.User;
import com.shred.sqlSession.SqlSession;
import com.shred.sqlSession.SqlSessionFactory;
import com.shred.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class IPersistenceTest {

    SqlSessionFactory factory;
    SqlSession sqlSession;
    IUserDao userMapper;


    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(1);
        user.setUsername("罗罗诺亚·索隆" + System.currentTimeMillis());
        int update = userMapper.update(user);
        System.out.println("updated row count: " + update +
                "\n updated username :" + user.getUsername());

        //此处没有事务控制，update立即提交
        //重新查一下 判断是否相等
        User byCondition = userMapper.findById(new User(1));
        System.out.println(byCondition.getUsername().equals(user.getUsername()));
    }

    @Test
    public void testInsert() {
        // id为101 的用户
        User user = new User(
                101,
                "哈哈101"
        );

        //查一下，判断是否为空
        User byId = userMapper.findById(user);
        System.out.println("before insert:" + byId);

        int insert = userMapper.insert(user);
        System.out.println("inserted row count:" + insert);

        //查一下，判断是否为空
        User byId2 = userMapper.findById(user);
        System.out.println("after insert: " + byId2);

    }

    @Test
    public void testDelete() {
        User user = new User(
                101,
                "哈哈101"
        );

        //查一下，验证时原本存在
        User byId = userMapper.findById(user);
        Assert.assertNotNull(byId);

        int delete = userMapper.delete(user);
        System.out.println("deleted row count:" + delete);

        //查一下，判断是否为空
        User byId2 = userMapper.findById(user);
        Assert.assertNull(byId2);

    }

    @Before
    public void init() throws PropertyVetoException, DocumentException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        factory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        sqlSession = factory.openSession();
        userMapper = sqlSession.getMapper(IUserDao.class);
    }

    @Test
    public void test() throws IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {


        User user = new User();
        user.setId(1);
        user.setUsername("zhangsan");
        User user2 = sqlSession.selectOne("user.selectOne", user);
        System.out.println(user2);


        List<Object> objects = sqlSession.selectList("user.selectList");
        System.out.println(objects);

    }

    @Test
    public void testProxyDao() throws PropertyVetoException, DocumentException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = factory.openSession();

        IUserDao mapper = sqlSession.getMapper(IUserDao.class);
        List<User> users = mapper.findAll();

        users.forEach(System.out::println);

        User user = new User();
        user.setId(1);
        user.setUsername("zhangsan");

        User selectOne = mapper.findByCondition(user);

        System.out.println(selectOne);
    }


}
