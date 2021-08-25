package com.lagou.rpc.provider.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerRegistry implements Watcher {

    @Value("${zk.ip}")
    String ip;

    @Value("${zk.port}")
    String  port;


    /**
     * 服务注册
     * @param address host:port
     */
    private void register(String address){
        new ZooKeeper(ip+":"+port, 5000, )
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }
}
