package com.shred.sqlSession;

public interface SqlSessionFactory {

    /**
     * 生产sqlSession
     * @return
     */
    public SqlSession openSession();
}
