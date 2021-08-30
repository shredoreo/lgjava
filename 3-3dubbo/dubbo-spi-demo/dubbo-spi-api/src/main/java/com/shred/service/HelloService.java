package com.shred.service;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

// value 指定默认实现，url中可已不指定参数
@SPI("dog")
public interface HelloService {
    String sayHello();

    /**
     * 动态选择
     * @param url
     * @return
     */
    @Adaptive
    String sayHello(URL url);
}
