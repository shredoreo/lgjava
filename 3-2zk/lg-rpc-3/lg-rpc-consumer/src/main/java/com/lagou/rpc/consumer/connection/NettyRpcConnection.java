package com.lagou.rpc.consumer.connection;

import com.lagou.rpc.constant.Const;
import com.lagou.rpc.consumer.RpcClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
@Component
public class NettyRpcConnection {

    /**
     * 建立连接的rpc客户端
     */
    HashMap<String, RpcClient> hostClientMap = new HashMap<>();

    public HashMap<String, RpcClient> getHostClientMap() {
        return hostClientMap;
    }

    public void setHostClientMap(HashMap<String, RpcClient> hostClientMap) {
        this.hostClientMap = hostClientMap;
    }

    public void prepareNettyRpcConn(HashMap<String, String> hostMap) {
        hostMap.forEach((k, v)->{

            hostClientMap.put(k,  createRpcConnection(k));

        });
    }

    public  RpcClient createRpcConnection(String serverAddress) {
        String[] split = serverAddress.split(Const.DELIMIT);
        String ip = split[0];
        String port = split[1];

        //创建连接
        RpcClient rpcClient = new RpcClient(ip, Integer.parseInt(port));
        System.out.println(">>>rpc🔗🔗连接建立："+serverAddress);
        //保存连接
        return rpcClient;
    }

}
