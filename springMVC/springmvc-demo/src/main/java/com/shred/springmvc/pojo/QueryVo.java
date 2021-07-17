package com.shred.springmvc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor
public class QueryVo {
    private String email;
    private String phone;

    private User user;

}
