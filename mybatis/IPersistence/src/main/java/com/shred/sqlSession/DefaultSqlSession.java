package com.shred.sqlSession;

import com.shred.pojo.Configuration;
import com.shred.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private SimpleExecutor simpleExecutor = new SimpleExecutor();;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

        //完成对SimpleExecutor对query
        simpleExecutor = new SimpleExecutor();
        List<Object> query = simpleExecutor.query(configuration, mappedStatement, params);

        return (List<E>) query;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws IllegalAccessException, ClassNotFoundException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {

        List<Object> objects = selectList(statementId, params);
        if (objects.size() == 0 ){
          return null;
        } else if (objects.size() == 1){
            return (T) objects.get(0);
        } else {
            throw new RuntimeException("查询结果为空，或返回结果过多");
        }

    }

    @Override
    public int insert(String statement, Object... params) throws IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statement);

        return simpleExecutor.insert(configuration, mappedStatement, params);
    }

    @Override
    public int update(String statement, Object... params) throws IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {

        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statement);

        return simpleExecutor.update(configuration, mappedStatement, params);
    }

    @Override
    public int delete(String statement, Object... params) throws IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statement);

        return simpleExecutor.delete(configuration, mappedStatement, params);
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        //使用jdk动态代理为Dao接口生成实现类

        Object o = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                // 为了通过动态代理调用具体对mapper，
                // 约定：namespace为dao接口全类名  sqlId为方法名
                String daoClassName = method.getDeclaringClass().getName();
                String methodName = method.getName();

                String statementId = daoClassName + "." + methodName;

                //获取方法返回值类型
                Type genericReturnType = method.getGenericReturnType();

                MappedStatement ms = configuration.getMappedStatement(statementId);
                //根据指令类型判断拦截的方法
                switch (ms.getSqlCommand()){
                    case SELECT:
                        //此处简单地根据返回值是否带泛型判断调用的是selectList还是selectOne
                        if (genericReturnType instanceof ParameterizedType) {
                            return selectList(statementId, args);
                        } else {
                            return selectOne(statementId, args);
                        }
                    case UPDATE:
                        return update(statementId, args);
                    case DELETE:
                        return delete(statementId, args);
                    case INSERT:
                        return insert(statementId, args);
                    default:
                        return null;
                }
            }
        });

        return (T) o;
    }
}
