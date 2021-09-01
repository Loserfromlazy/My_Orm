package com.iorm.sqlSession;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface SqlSession {

    /**
     * 查询所有
     * @param statementId
     * @param <E>
     * @return
     */
    <E> List<E> selectList(String statementId,Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException;

    /**
     * 查询单个
     * @param statementId
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     */
    <T> T selectOne(String statementId,Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException;

    /**
     * 为dao生成代理实现类
     * @param mapperClass
     * @param <T>
     * @return
     */
    <T> T getMapper(Class<?> mapperClass);
}
