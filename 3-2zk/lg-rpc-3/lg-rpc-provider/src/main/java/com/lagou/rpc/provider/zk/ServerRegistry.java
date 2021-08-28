package com.lagou.rpc.provider.zk;

import com.lagou.rpc.constant.Const;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class ServerRegistry implements Watcher {

    @Value("${zk.ip}")
    String ip;

    @Value("${zk.port}")
    int  port;

    ZkClient zkClient;


    @PostConstruct
    private void connect(){
        zkClient = new ZkClient(ip+":"+ port);
        System.out.println("zk连接建立..");
        if (!zkClient.exists(Const.NODE_PARENT)){
            System.out.println("创建服务端根结点");
            zkClient.createPersistent(Const.NODE_PARENT);
        }


    }


    /**
     * 服务注册
     * @param address host:port
     */
    public void register(String address) throws IOException, KeeperException, InterruptedException {

        createNode(address, "0");

    }

    public void createNode(String path, Object value) {
        path = Const.NODE_PARENT + "/"+ path;

        //创建临时节点

        zkClient.createEphemeral(path);
        System.out.println("zk创建节点" + path);
        zkClient.writeData(path, value);
        System.out.println("节点" + path + " 写入值 " + value);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        //处理连接事件
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected){
            System.out.println("process 方法执行了");
        }
    }
}
