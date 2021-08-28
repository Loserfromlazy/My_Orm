package com.iorm.config;

import com.iorm.pojo.Configuration;
import com.iorm.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        List<Element> list = rootElement.selectNodes("//select");
        list.stream().forEach(element -> {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String paramerType = element.attributeValue("paramType");
            String sqlText = element.getTextTrim();
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParamterType(paramerType);
            mappedStatement.setSql(sqlText);
            String key = namespace+"."+id;
            configuration.getMappedStatementHashMap().put(key,mappedStatement);
        });
    }
}
