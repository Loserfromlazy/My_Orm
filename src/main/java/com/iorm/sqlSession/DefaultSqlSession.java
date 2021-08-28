package com.iorm.sqlSession;

import com.iorm.pojo.Configuration;
import com.iorm.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 查询所有
     * @param statementId
     * @param params
     * @param <E>
     * @return
     * @throws SQLException
     */
    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException {
        //完成对simpleExecutor的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementHashMap().get(statementId);
        List<Object> query = simpleExecutor.query(configuration, mappedStatement, params);
        return (List<E>) query;
    }

    /**
     * 查询单个
     * @param statementId
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> T selectOne(String statementId, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException {
        List<Object> objects = selectList(statementId, params);
        if (objects.size()==1){
            return (T) objects.get(0);
        }else {
            throw new RuntimeException("查询结果为空或者结果过多");
        }
    }
}
