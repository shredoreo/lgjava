package com.lagou.rpc.provider.zk;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
    public void register(String address) throws IOException, KeeperException, InterruptedException {
        String connectString = ip + ":" + port;
       /* ZooKeeper zooKeeper = new ZooKeeper(connectString, 5000, new ServerRegistry());
        //创建服务端对应端的临时节点
        String tmpNode = zooKeeper.create(address,
                ("节点内容" + address).getBytes(StandardCharsets.UTF_8),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL
        );
                System.out.println("节点被创建: " + tmpNode);

*/
        ZkClient zkClient = new ZkClient(connectString);
        zkClient.createEphemeral("/rpc/"+ address);

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        //处理连接事件
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected){
            System.out.println("process 方法执行了");
        }
    }
}
