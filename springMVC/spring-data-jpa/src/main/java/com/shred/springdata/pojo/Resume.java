package com.shred.springdata.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 1、使用注解建立实体类和数据表之间的映射关系
 * 2、属性和字段之间 的映射
 *
 */
@Data@AllArgsConstructor@NoArgsConstructor
@Entity@Table(name = "tb_resume")
public class Resume {
    /**
     * GenerationType.IDENTITY mysql自增主键
     * GenerationType.SEQUENCE 依靠序列产生主键 oracle
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "phone")
    private String phone;

}
