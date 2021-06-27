package com.shred.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private Date birthDay;

    //权限列表
    private List<Role> roleList;

    private List<Order> orderList;
}
