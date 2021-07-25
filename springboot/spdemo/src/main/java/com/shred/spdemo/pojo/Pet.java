package com.shred.spdemo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data@NoArgsConstructor@AllArgsConstructor
public class Pet {
    private String type;
    private String name;
}
