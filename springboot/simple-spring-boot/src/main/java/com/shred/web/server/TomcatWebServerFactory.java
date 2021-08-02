package com.shred.web.server;

import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.FactoryBean;

public class TomcatWebServerFactory implements FactoryBean<Tomcat> {

    @Override
    public Tomcat getObject() throws Exception {

        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
