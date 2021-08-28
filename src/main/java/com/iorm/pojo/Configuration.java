package com.iorm.pojo;


import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    //数据源对象
    private DataSource dataSource;

    /**
     * key: statementId value :封装好的mappedStatement对象
     */
    private Map<String, MappedStatement> mappedStatementHashMap = new HashMap<>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMappedStatementHashMap() {
        return mappedStatementHashMap;
    }

    public void setMappedStatementHashMap(Map<String, MappedStatement> mappedStatementHashMap) {
        this.mappedStatementHashMap = mappedStatementHashMap;
    }
}
