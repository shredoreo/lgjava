package com.shred.test;

import com.shred.dao.UserDao;
import com.shred.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisTest {
    @Test
    public void testForEach() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

       int [] ids = new int[]{1,2,3};
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> all = mapper.findByIds(ids);
        System.out.println(all);

        sqlSession.close();

    } @Test
    public void testDynSql() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        User user = new User();
        user.setId(1);
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        User all = mapper.findByCondition(user);
        System.out.println(all);

        sqlSession.close();

    }


    @Test
    public void testProxy() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> all = mapper.findAll();
        System.out.println(all);

        sqlSession.close();

    }

    @Test
    public void test1() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        //默认开启一个事务，但不会自动提交
        //在进行cud要手动提交事务
        //可使用带参数构造器 创建自动提交带session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<User> userList = sqlSession.selectList("user.findAll");
        System.out.println(userList);

        sqlSession.close();

    }

    @Test
    public void test2() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        User tom = new User(5, "tom");
        int insert = sqlSession.insert("user.saveUser", tom);
        sqlSession.commit();

        sqlSession.close();

    }

    @Test
    public void testUpdate() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        User tom = new User(3, "jerry");
         sqlSession.update("user.update", tom);
        sqlSession.commit();

        sqlSession.close();

    }

    @Test
    public void testDelete() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

         sqlSession.delete("user.delete", 5);
        sqlSession.commit();

        sqlSession.close();

    }
}
