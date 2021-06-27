package com.shred.test;

import com.shred.dao.IUserDao;
import com.shred.io.Resources;
import com.shred.pojo.User;
import com.shred.sqlSession.SqlSession;
import com.shred.sqlSession.SqlSessionFactory;
import com.shred.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class IPersistenceTest {

    @Test
    public void test() throws PropertyVetoException, DocumentException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = factory.openSession();

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
        System.out.println(users);


        User user = new User();
        user.setId(1);
        user.setUsername("zhangsan");

        User selectOne = mapper.findByCondition(user);

        System.out.println(selectOne);
    }


}
