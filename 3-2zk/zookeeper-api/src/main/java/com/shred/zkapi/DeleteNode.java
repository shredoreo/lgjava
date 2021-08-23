package com.shred.zkapi;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class DeleteNode implements Watcher {

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException {

        zooKeeper = new ZooKeeper("tx1:2181", 5000, new DeleteNode());
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
                deleteNodeSync();
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void deleteNodeSync() throws KeeperException, InterruptedException {
        String name = "/shred-persistent/s-p-1";
        Stat exists = zooKeeper.exists(name, false);
        System.out.println(exists == null? "不存在":"存在");

        if (exists !=null){
            zooKeeper.delete(name, -1);
        }

        Stat stat = zooKeeper.exists(name, false);
        System.out.println(stat == null? "不存在":"存在");
    }


}
