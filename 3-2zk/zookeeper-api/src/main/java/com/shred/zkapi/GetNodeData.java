package com.shred.zkapi;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GetNodeData implements Watcher {

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException {

        zooKeeper = new ZooKeeper("tx1:2181", 5000, new GetNodeData());
        System.out.println(zooKeeper.getState());

        //表示会话真正建立
        System.out.println("=========Client Connected to zookeeper==========");

        //维持会话，不让main方法结束，也就不让session关闭
        Thread.sleep(Integer.MAX_VALUE);

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {

            /*
                子节点列表发生变更时，服务端发送 nodeChildrenChanged 事件
                要重新互殴去子节点列表，同时，通知是一次性的，需要反复注册监听
             */
            if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                //重新获取，反复注册监听，下一次变更时才嫩获取到事件
                getChildren();
            }

            // SyncConnected
            if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                System.out.println("process 方法执行了");
                getNodeData();
                getChildren();
            }

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取节点的子节点列表
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void getChildren() throws KeeperException, InterruptedException {
        /*
            path:路径
            watch:是否要启动监听，当子节点列表发生变化，会触发监听
             zooKeeper.getChildren(path, watch);
        */
        List<String> children = zooKeeper.getChildren("/shred-persistent", true);
        System.out.println(children);
    }

    /**
     * 获取节点数据
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void getNodeData() throws KeeperException, InterruptedException {
        // stat :节点状态，null表示获取最新版本的数据
        byte[] data = zooKeeper.getData("/shred-persistent", false, null);
        System.out.println(new String(data));
    }

}
