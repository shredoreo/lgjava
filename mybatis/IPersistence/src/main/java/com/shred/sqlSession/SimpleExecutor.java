package com.shred.sqlSession;

import com.shred.config.BoundSql;
import com.shred.pojo.Configuration;
import com.shred.pojo.MappedStatement;
import com.shred.utils.GenericTokenParser;
import com.shred.utils.ParameterMapping;
import com.shred.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor  implements Executor{


    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException {
        //1 注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();

        //2 获取sql语句：select * from user where id = #{id} and username = #{username}
        //            -> select * from user where id = ? and username = ?
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        //3 获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getParseSql());

        //4 设置参数
        String parameterType = mappedStatement.getParameterType();
        Class<?> parameterClass =  getClassType(parameterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射 获取实体属性值®
            Field declaredField = parameterClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            //反射 获取传入对象params[0] 对content属性值
            Object o = declaredField.get(params[0]);

            //设置占位的值
            preparedStatement.setObject(i+1 , o);


        }

        //5 执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultClass = getClassType(resultType);
        ArrayList<Object> objects = new ArrayList<>();

        //6 封装返回结果
        while (resultSet.next()){
            Object o = resultClass.newInstance();

            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //字段名 下标从1开始
                String columnName = metaData.getColumnName(i);

                //字段值
                Object value = resultSet.getObject(columnName);

                //使用反射，根据数据库表和实体的对应关系，封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultClass);

                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);

            }
            objects.add(o);
        }

        return (List<E>) objects;
    }

    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        if (parameterType != null){
            Class<?> aClass = Class.forName(parameterType);
            return aClass;
        }
        return null;
    }

    /**
     *  完成对#{}的解析：1替换成？ 2 血洗出
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        //标记处理类，处理#{}
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);

        String parse = genericTokenParser.parse(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(parse, parameterMappings);

        return boundSql;
    }
}
