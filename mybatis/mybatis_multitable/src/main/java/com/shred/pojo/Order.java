package com.shred.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order implements Serializable {
    private Integer id;
    private Date orderTime;
    private Double total;

    private User user;

}
