package com.iorm.sqlSession;

import com.iorm.config.XMLConfigBuilder;
import com.iorm.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(InputStream inputStream) throws DocumentException, PropertyVetoException {
        //1.使用dom4j解析配置文件，将解析出来的对象封装到Configuration中
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(inputStream);
        //2.创建sqlSessionFactory对象 ：工厂类 生产sqlSession会话对象
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }
}
