package com.shred.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data@NoArgsConstructor@AllArgsConstructor
public class Role implements Serializable {
    private Integer id;
    private String roleName;
    private String roleDesc;
}
