package com.shred.pojo;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
    /**
     * 直接创建连接
     */
    private DataSource dataSource;

    /**
     * key:statementId val: 封装好的statement
     */
    Map<String,MappedStatement> mappedStatementMap = new HashMap<>();

    public void setDataSource(ComboPooledDataSource c3p0) {
        this.dataSource  = c3p0;

    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }
}
