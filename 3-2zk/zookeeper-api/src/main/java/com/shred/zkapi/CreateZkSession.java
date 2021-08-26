package com.shred.zkapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class CreateZkSession implements Watcher {

    //countDownLatch这个类使一个线程等待,主要不让main方法结束
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        /*
            客户端可以通过创建一个zk实例来连接zk服务器
            new Zookeeper(connectString,sessionTimeOut,Watcher)
            connectString: 连接地址:IP:端口
            sessionTimeOut:会话超时时间:单位毫秒
            Watcher:监听器(当特定事件触发监听时，zk会通过watcher通知到客户端)
        */
        ZooKeeper zooKeeper = new ZooKeeper("tx1:2181", 5000, new CreateZkSession());
        System.out.println(zooKeeper.getState());

        countDownLatch.await();
        //表示会话真正建立
        System.out.println("=========Client Connected to zookeeper==========");

    }

    // 当前类实现了Watcher接口，重写了process方法，
    // 该方法负责处理来自Zookeeper服务端的 watcher通知，
    // 在收到服务端发送过来的 SyncConnected 事件之后，解除主程序在CountDownLatch上 的等待阻塞，
    // 至此，会话创建完毕
    @Override
    public void process(WatchedEvent watchedEvent) {
        // SyncConnected
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected){
            System.out.println("process 方法执行了");
            //解除主程序在 CountDownLatch 的等待阻塞
            countDownLatch.countDown();
        }
    }
}
