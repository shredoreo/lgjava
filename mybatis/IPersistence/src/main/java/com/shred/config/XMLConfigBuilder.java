package com.shred.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.shred.io.Resources;
import com.shred.pojo.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {

    private Configuration configuration;

    public XMLConfigBuilder(){
        this.configuration = new Configuration();
    }

    /**
     * 给方法解析配置文件,封装成configuration
     * @param is
     * @return
     */
    public Configuration pasrseConfig(InputStream is ) throws DocumentException, PropertyVetoException {

        Document document = new SAXReader().read(is);
        Element rootElement = document.getRootElement();
        // the double / here means any inner level of the xml
        List<Element> list = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");

            properties.setProperty(name, value);
        }


        ComboPooledDataSource c3p0 = new ComboPooledDataSource();
        c3p0.setDriverClass(properties.getProperty("driverClass"));
        c3p0.setJdbcUrl(properties.getProperty("jdbcUrl"));
        c3p0.setUser(properties.getProperty("username"));
        c3p0.setPassword(properties.getProperty("password"));

        //put into configuration
        configuration.setDataSource(c3p0);

        //parse mapper.xml: 拿到路径，加载成字节流
        List<Element> mapperList = rootElement.selectNodes("//mapper");

        for (Element element : mapperList) {
            String mapperPath = element.attributeValue("resource");
            InputStream resourceAsStream = Resources.getResourceAsStream(mapperPath);

            //因为一个mapperConifig文件中存在多个语句，也就会有多个MappedStatement对象，
            //这些ms对象都需要存入configuration中，所以此处应传入configuration作为入参
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);


            xmlMapperBuilder.parser(resourceAsStream);

        }


        return configuration ;
    }
}
