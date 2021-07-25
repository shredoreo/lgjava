package com.shred.spdemo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data@NoArgsConstructor@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "person") // 实现属性的批量注入
public class Person {
    private int id; //id
    private String name; //名称
    private List hobby; //爱好
    private String[] family; //家庭成员
    private Map map;
    private Pet pet; //宠物 // 省略属性getXX()和setXX()方法

}
