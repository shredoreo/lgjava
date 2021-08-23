package com.shred.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class UseZkClient {
    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient("tx1:2181");
        System.out.println("会话创建了。。");

        api(zkClient);

        getNodeChildren(zkClient);

        createNode(zkClient);

        deleteNode(zkClient);

    }

    private static void api(ZkClient zkClient) throws InterruptedException {
        String path = "/shred-zkClient-Ep";
        boolean exists = zkClient.exists(path);
        if (!exists){
            zkClient.createEphemeral(path, "123");
        }

        //读取
        Object o = zkClient.readData(path);
        System.out.println(o);

        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println(s + " 节点数据发生变化，变化后 "+o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println(s + " 节点被删除");
            }
        });

        zkClient.writeData(path, "444");
        Thread.sleep(1000);

        zkClient.delete(path);
        Thread.sleep(1000);

    }

    private static void getNodeChildren(ZkClient zkClient) throws InterruptedException {
        String path = "/shred-pppp";
        System.out.println("=====测试子节点 ===" +path);
        if (zkClient.exists(path)){
            zkClient.deleteRecursive(path);
        }

        //注册监听，可以对不存在的节点进行子节点变更监听，只要该节点子节点列表、或该节点本身被创建或删除，都会触发更新
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            /*
            s: parentPath
            list；变化后子节点的 列表
             */
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println(s + "子节点列表发生变化，变化后："+ list);
            }
        });


        zkClient.createPersistent(path);
        Thread.sleep(1000);

        zkClient.createPersistent(path +"/n1");

    }

    private static void deleteNode(ZkClient zkClient) {
        String path = "/shred-p1/p1-1";
        System.out.println("==== 测试删除====" +path);
        //先创建子节点，再删除父节点
        zkClient.createPersistent(path + "/p1-1-1");
        zkClient.deleteRecursive(path);
        System.out.println("递归删除节点 :"+path );
    }

    private static void createNode(ZkClient zkClient) {
        String path = "/shred-p1/p1-1";
        System.out.println("===测试创建===="+ path);
        zkClient.createPersistent(path, true);
        System.out.println("递归创建节点"+path);
    }
}
