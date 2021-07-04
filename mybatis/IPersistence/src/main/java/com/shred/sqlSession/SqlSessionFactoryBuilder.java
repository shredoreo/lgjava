package com.shred.sqlSession;

import com.shred.config.XMLConfigBuilder;
import com.shred.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder  {

    public SqlSessionFactory build(InputStream in) throws PropertyVetoException, DocumentException {
        //1 使用dom4j解析配置文件 封装到configuration中
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.pasrseConfig(in);

        //2 创建SqlSessionFactory
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);

        return defaultSqlSessionFactory;
    }

}
