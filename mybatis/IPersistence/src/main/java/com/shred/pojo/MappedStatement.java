package com.shred.pojo;

import com.shred.enums.SqlCommand;

public class MappedStatement {

    /**
     * idbiaoshi
     */
    private String id;

    private String resultType;

    private String parameterType;

    private String sql;

    private SqlCommand sqlCommand;


    public SqlCommand getSqlCommand() {
        return sqlCommand;
    }

    public void setSqlCommand(SqlCommand sqlCommand) {
        this.sqlCommand = sqlCommand;
    }

    public MappedStatement(String id, String resultType, String parameterType, String sql, SqlCommand sqlCommand) {
        this.id = id;
        this.resultType = resultType;
        this.parameterType = parameterType;
        this.sql = sql;
        this.sqlCommand = sqlCommand;
    }

    public MappedStatement(String id, String resultType, String parameterType, String sql) {
        this.id = id;
        this.resultType = resultType;
        this.parameterType = parameterType;
        this.sql = sql;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
