package com.shred.zkapi;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class CreateZkNode implements Watcher {

    //countDownLatch这个类使一个线程等待,主要不让main方法结束
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException {

        zooKeeper = new ZooKeeper("tx1:2181", 5000, new CreateZkNode());
        System.out.println(zooKeeper.getState());

        countDownLatch.await();
        //表示会话真正建立
        System.out.println("=========Client Connected to zookeeper==========");

    }

    /**
     * 同步创建节点
     */
    private static void createNodeSync() throws KeeperException, InterruptedException {
        /**

         path: 节点创建路径 以/开头
         data[]：节点保存的数据，时nyte类型
         acl：节点创建的权限信息(4种类型)
             ANYONE_ID_UNSAFE : 表示任何人
             AUTH_IDS :此ID仅可用于设置ACL。它将被客户机验证的ID替
             OPEN_ACL_UNSAFE :这是一个完全开放的ACL(常用)--> world:anyone
             CREATOR_ALL_ACL :此ACL授予创建者身份验证ID的所有权限
         createMode：创建节点的类型(4种类型)
             PERSISTENT:持久节点
             PERSISTENT_SEQUENTIAL:持久顺序节点
             EPHEMERAL:临时节点
             EPHEMERAL_SEQUENTIAL:临时顺序节点
         String node = zookeeper.create(path,data,acl,createMode);
         */
        String node_persist = zooKeeper.create(
                "/shred-persistent",
                "节点内容".getBytes(StandardCharsets.UTF_8),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT
        );

        String node_tmp = zooKeeper.create(
                "/shred-ephemeral",
                "节点内容".getBytes(StandardCharsets.UTF_8),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL
        );
        String node_persist_seq = zooKeeper.create(
                "/shred-persistent_sequential",
                "节点内容".getBytes(StandardCharsets.UTF_8),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL
        );

        //
        System.out.println("创建了三个节点");
        System.out.println(node_persist);
        System.out.println(node_tmp);
        System.out.println(node_persist_seq);


    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        // SyncConnected
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected){
            System.out.println("process 方法执行了");

            // 创建节点
            try {
                createNodeSync();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            //解除主程序在 CountDownLatch 的等待阻塞
            countDownLatch.countDown();
        }
    }
}
