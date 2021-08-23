package com.lagou.rpc.provider.register;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RegisterCenter implements IRegitsterCenter{
    /**
     * 服务 -> 地址
     */
    public static ConcurrentHashMap<String, List<String>> serviceRegistry = new ConcurrentHashMap<String,List<String>>();

    /**
     * 注册服务
     * @param serviceName
     * @param address
     */
    @Override
    public void register(String serviceName, String address) {
        System.out.println("注册服务 ："+ serviceName + "  地址:"+ address);
        serviceRegistry.computeIfAbsent(serviceName, k -> new ArrayList<>())
                .add(address);
    }


}
