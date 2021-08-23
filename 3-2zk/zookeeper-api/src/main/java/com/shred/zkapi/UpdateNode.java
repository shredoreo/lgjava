package com.shred.zkapi;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

public class UpdateNode implements Watcher {

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException {

        zooKeeper = new ZooKeeper("tx1:2181", 5000, new UpdateNode());
        System.out.println(zooKeeper.getState());

        //表示会话真正建立
        System.out.println("=========Client Connected to zookeeper==========");

        //维持会话，不让main方法结束，也就不让session关闭
        Thread.sleep(Integer.MAX_VALUE);

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {

            // SyncConnected
            if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                System.out.println("process 方法执行了");

                updateNodeSync();
            }

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新数据节点
     */
    private void updateNodeSync() throws KeeperException, InterruptedException {
        getLatestData("/shred-persistent");

        /*
            path:路径
            data:要修改的内容 byte[]
            version:为-1，表示对最新版本的数据进行修改
            zooKeeper.setData(path, data,version);
        */
        Stat stat = zooKeeper.setData("/shred-persistent",(new Date()+"客户端修改了").getBytes(StandardCharsets.UTF_8), -1);

        getLatestData("/shred-persistent");

    }

    private void getLatestData(String nodeName) throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData(nodeName, false, null);
        System.out.println("节点值："+new String(data));
    }

}
