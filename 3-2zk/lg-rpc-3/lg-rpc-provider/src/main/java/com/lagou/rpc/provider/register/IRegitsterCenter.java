package com.lagou.rpc.provider.register;

public interface IRegitsterCenter {

    /**
     * 注册服务
     * @param serviceName
     * @param servicAddress
     */
    void register(String serviceName, String  servicAddress);

}
