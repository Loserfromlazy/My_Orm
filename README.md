# 自定义持久层框架

此项目是Mybatis的学习项目，只是用来了解加深对Mybatis的了解的，仅简单实现了Mybatis。具体内容见我的[学习笔记](https://www.cnblogs.com/yhr520/p/12550205.html)

使用方法如下：

resources下编写SqlMapConfig.xml配置文件
~~~ xml
<configuration>
<!--    配置数据源-->
    <dataSource>
        <property name="driverClass" value="com.mysql.cj.jdbc.Driver"></property>
        <property name="jdbcUrl" value="jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&amp;characterEncoding=UTF8&amp;useSSL=false&amp;serverTimezone=Asia/Shanghai"></property>
        <property name="username" value="root"></property>
        <property name="password" value="root"></property>
    </dataSource>
<!--    存放mapper.xml的全路径-->
    <mapper resource="UserMapper.xml"></mapper>

</configuration>
~~~
resources下编写UserMapper.xml配置文件
~~~xml
<mapper namespace="user">
<!--    sql的唯一标识：namespace和id组成  ：statementId -->
    <select id="selectList" resultType="com.iorm.pojo.User" >
        select * from user
    </select>

<!--    User user = new User()
        user.setId(1);
-->
    <select id ="selectOne" resultType="com.iorm.pojo.User" paramType="com.iorm.pojo.User">
        select * from user where id = #{id} and username = #{username}
    </select>
</mapper>
~~~
测试方法的编写
~~~java
/**
 * 测试类 测试orm框架
 */
public class Test1 {

    @Test
    public void test() throws PropertyVetoException, DocumentException, SQLException, IntrospectionException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        //测试第一版orm
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSqlSession();

        User user = new User();
        user.setId(1);
        user.setUsername("张老师");
//        测试单个
//        User userSelect = sqlSession.selectOne("user.selectOne",user);
//        System.out.println(userSelect.toString());
//        测试所有
//        List<Object> selectList = sqlSession.selectList("user.selectList");
//        selectList.stream().forEach(System.out::println);
//        测试代理
        //测试第二版
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        List<User> all = userDao.findAll();
        if (all == null){
            System.out.println("all为null");
        }else {
            System.out.println(all.size());
        }
        all.stream().forEach(System.out::println);
        User byCondition = userDao.findByCondition(user);
        System.out.println(byCondition);
    }
}

~~~
