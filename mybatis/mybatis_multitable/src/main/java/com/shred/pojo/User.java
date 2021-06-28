package com.shred.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Data
@Table//表
public class User implements Serializable {
    @Id//？
    @GeneratedValue(strategy = GenerationType.IDENTITY)//主键生成策略：自增
    private Integer id;

    private String username;
   /* private String password;
    private Date birthDay;

    //权限列表
    private List<Role> roleList;

    private List<Order> orderList;*/
}
