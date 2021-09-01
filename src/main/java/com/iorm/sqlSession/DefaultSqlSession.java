package com.iorm.sqlSession;

import com.iorm.pojo.Configuration;
import com.iorm.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.*;
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

    /**
     * 为dao接口实现代理实现类
     * @param mapperClass
     * @param <T>
     * @return
     */
    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        //使用JDK动态代理为dao生成代理对象，并返回
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                //底层执行jdbc方法 根据不同情况来调用selectList后者selectOne
                //准备参数 1.statementId：sql语句唯一标识
                String methodName = method.getName();//方法名 eg:findAll
                String className = method.getDeclaringClass().getName();//接口全限定名
                String statementId = className+"."+methodName;
                //参数 2.params :objects
                //获取被调用方法的返回值类型
                Type genericReturnType = method.getGenericReturnType();
                //判断是否进行了泛型类型参数化 即返回值是否有泛型
                if (genericReturnType instanceof ParameterizedType){
                    List<Object> objectList = selectList(statementId, objects);
                    return objectList;
                }else {
                    Object objectOne = selectOne(statementId, objects);
                    return objectOne;
                }
            }
        });
        return (T) proxyInstance;
    }
}
