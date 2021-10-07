package com.shred.sc.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="shred_auth_code")
public class AuthCode {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String code;
    private Date createTime;
    private Date expireTime;
}
