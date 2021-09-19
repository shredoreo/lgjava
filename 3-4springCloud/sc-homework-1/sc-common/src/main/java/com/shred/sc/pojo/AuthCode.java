package com.shred.sc.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class AuthCode {

    private Integer id;
    private String email;
    private String code;
    private Date createTime;
    private Date expireTime;
}
