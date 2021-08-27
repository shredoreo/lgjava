package com.lagou.rpc.consumer.discover;

import org.I0Itec.zkclient.ZkClient;

public interface IDiscover {
    ZkClient connect();
    String discover(String serviceName);
}
