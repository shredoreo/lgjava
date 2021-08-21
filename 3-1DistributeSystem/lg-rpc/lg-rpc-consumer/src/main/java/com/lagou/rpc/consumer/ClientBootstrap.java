package com.lagou.rpc.consumer;

import com.lagou.rpc.api.IUserService;
import com.lagou.rpc.consumer.proxy.RpcClientProxy;
import com.lagou.rpc.pojo.User;

public class ClientBootstrap {
    public static void main(String[] args) {
        IUserService proxy = (IUserService) RpcClientProxy.createProxy(IUserService.class);
        System.out.println(11111);
        User byId = proxy.getById(1);
        System.out.println(byId);

    }
}
