package com.shred.spdemo.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("acme.my-person.person")
public class OwnerProperties {
    private String firstName;
}