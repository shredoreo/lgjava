package com.shred.sc.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="shred_token")
public class Token {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String token;
}
