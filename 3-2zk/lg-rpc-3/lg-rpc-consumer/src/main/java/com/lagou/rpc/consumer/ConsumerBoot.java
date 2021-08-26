package com.lagou.rpc.consumer;

import com.lagou.rpc.consumer.connection.NettyConnection;
import com.lagou.rpc.consumer.zk.ZkUtilConsumer;

import java.util.Map;

public class ConsumerBoot {

    public static void main(String[] args) {

        //获取zk连接
        ZkUtilConsumer zkUtilConsumer = new ZkUtilConsumer();
        zkUtilConsumer.connect();
        //从zk上获取连接的ip和port
        Map<String, String> hostAndNodeMap = zkUtilConsumer.getChildrenHost(false);
        NettyConnection.createConnection(hostAndNodeMap);
    }
}
