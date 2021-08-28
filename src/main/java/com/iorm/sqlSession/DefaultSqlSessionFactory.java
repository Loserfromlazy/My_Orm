package com.iorm.sqlSession;

import com.iorm.pojo.Configuration;

public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public SqlSession openSqlSession() {
        return new DefaultSqlSession(configuration);
    }
}
