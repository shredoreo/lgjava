package com.shred.spdemo.pojo;

import lombok.Data;

import java.net.InetAddress;

@Data
public class ThirdPartComponent {

    private Boolean enabled;

    private InetAddress remoteAddress;
}
