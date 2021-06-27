package com.shred.sqlSession;

import com.shred.pojo.Configuration;

public class DefaultSqlSessionFactory implements SqlSessionFactory{
    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;

    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
