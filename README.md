# 自定义持久层框架
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
public class Test1 {

    @Test
    public void test() throws PropertyVetoException, DocumentException, SQLException, IntrospectionException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
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
        List<Object> selectList = sqlSession.selectList("user.selectList");
        selectList.stream().forEach(System.out::println);
    }
}
~~~
