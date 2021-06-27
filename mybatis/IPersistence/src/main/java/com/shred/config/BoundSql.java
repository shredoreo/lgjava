package com.shred.config;

import com.shred.utils.ParameterMapping;

import java.util.List;

public class BoundSql {

    private String parseSql;

    private List<ParameterMapping> parameterMappingList;

    public BoundSql(String parseSql) {
        this.parseSql = parseSql;
    }

    public BoundSql(String parseSql, List<ParameterMapping> parameterMappingList) {
        this.parseSql = parseSql;
        this.parameterMappingList = parameterMappingList;
    }

    public String getParseSql() {
        return parseSql;
    }

    public void setParseSql(String parseSql) {
        this.parseSql = parseSql;
    }

    public List<ParameterMapping> getParameterMappingList() {
        return parameterMappingList;
    }

    public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
        this.parameterMappingList = parameterMappingList;
    }
}
