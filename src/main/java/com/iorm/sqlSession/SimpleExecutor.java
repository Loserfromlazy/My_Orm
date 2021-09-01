package com.iorm.sqlSession;

import com.iorm.config.BoundSql;
import com.iorm.pojo.Configuration;
import com.iorm.pojo.MappedStatement;
import com.iorm.utils.GenericTokenParser;
import com.iorm.utils.ParameterMapping;
import com.iorm.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException {
        //1. 注册驱动获取链接
        Connection connection = configuration.getDataSource().getConnection();
        //2. 获取sql语句
        String sql = mappedStatement.getSql();
        //转换sql语句 转换占位符，同时对#{}中的值进行解析存储
        BoundSql boundSql = getBoundSql(sql);
        //获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //设置参数
        String paramterType = mappedStatement.getParameterType();//获取到了参数实体的全路径
        Class<?> parameterTypeClass = getClassType(paramterType);//根据全路径获取Class对象
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);//取出集合中的每一个对象
            String content = parameterMapping.getContent();
            //反射
            Field declaredField = parameterTypeClass.getDeclaredField(content);//获取属性对象
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);//通过属性对象获取实体对象的值
            preparedStatement.setObject(i + 1, o);
        }
        //执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);
        ArrayList<Object> objects = new ArrayList<>();
        //封装返回结果集
        while (resultSet.next()) {
            Object o = resultTypeClass.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();//元数据：元数据中包含了结果中的字段名
            for (int i = 1; i < metaData.getColumnCount(); i++) {//metaData.getColumnCount()
                //字段名
                String columnName = metaData.getColumnName(i);
                //字段值
                Object value = resultSet.getObject(columnName);
                //使用反射或内省，根据数据库表和实体的对应关系，完成封装
                //PropertyDescriptor是内省库中的类 利用此类的有参创建对象 此类会根据resultTypeClass的columnName属性生成读写方法
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);//把value值封装到了对象中
            }
            objects.add(o);
        }


        return (List<E>) objects;
    }

    private Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        if (paramterType != null) {
            Class<?> aClass = Class.forName(paramterType);
            return aClass;
        }
        return null;
    }

    /**
     * 完成对#{}的解析 将#{}替代为? 同时 解析出#{}中的值进行存储
     *
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        //标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", tokenHandler);
        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        //#{}解析出来的参数名称
        List<ParameterMapping> parameterMappings = tokenHandler.getParameterMappings();
        return new BoundSql(parseSql, parameterMappings);
    }
}
