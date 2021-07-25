package com.shred.spdemo.config;

import com.shred.spdemo.pojo.ThirdPartComponent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThirdPartCompConfig {

    @Bean
    @ConfigurationProperties(prefix = "third")
    public ThirdPartComponent thirdPartComponent(){
        return new ThirdPartComponent();
    }

}
