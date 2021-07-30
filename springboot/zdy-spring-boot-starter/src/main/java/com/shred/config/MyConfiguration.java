package com.shred.config;

import com.shred.pojo.SimpleBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnBean(ConfigMarker.class)
@Configuration
public class MyConfiguration {

    static {
        System.out.println("MyConfiguration init ...");
    }

    @Bean
    public SimpleBean simpleBean(){
        return new SimpleBean();
    }

}
