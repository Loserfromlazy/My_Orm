package com.iorm.sqlSession;

import com.iorm.pojo.Configuration;
import com.iorm.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface Executor {
    /**
     * 执行查询
     * @param configuration
     * @param mappedStatement
     * @param params
     * @param <E>
     * @return
     * @throws SQLException
     */
    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement,Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException;

}
