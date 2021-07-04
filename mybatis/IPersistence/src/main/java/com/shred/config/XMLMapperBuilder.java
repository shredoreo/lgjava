package com.shred.config;

import com.shred.enums.SqlCommand;
import com.shred.pojo.Configuration;
import com.shred.pojo.MappedStatement;
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

    public void parser(InputStream in) throws DocumentException {
        Document document = new SAXReader().read(in);
        //mapper根结点
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");

        //获取所有子节点
        @SuppressWarnings("unchecked")
        List<Element> elements = rootElement.elements();

        for (Element element : elements) {

            //节点名称 update\delete\insert\select
            String name = element.getName();
            //sql指令类型
            SqlCommand cmd = SqlCommand.valueOf(name.toUpperCase());

            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sqlText = element.getTextTrim();

            MappedStatement mappedStatement = new MappedStatement(
                    id, resultType, parameterType, sqlText, cmd
            );

            String key = namespace + "." + id;
            configuration.getMappedStatementMap().put(key, mappedStatement);

        }

    }
}
