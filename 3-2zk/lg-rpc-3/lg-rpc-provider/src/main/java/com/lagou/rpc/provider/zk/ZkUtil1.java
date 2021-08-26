package com.lagou.rpc.provider.zk;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZkUtil1 {

    private static ZkClient ZK_CLIENT = null;

    /**
     * 根目录
     */
    private static final String PARENT_PATH = "/netty";

    /**
     * 本地储存
     */
    private static final Map<String, String> CHILDREN_NODE_MAP = new HashMap<>(16);

    public ZkUtil1() {
        connect();
    }

    public static ZkClient connect() {

        ZK_CLIENT = new ZkClient("127.0.0.1:2184");

        System.out.println("zk连接建立");

        //建立初始节点
        //判断是否存在
        boolean exists = ZK_CLIENT.exists(PARENT_PATH);
        if(!exists) {
            //节点不存在，创建临时节点
            ZK_CLIENT.createEphemeral(PARENT_PATH);
        }

        return ZK_CLIENT;
    }


    public void createNode(String path, String value) {

        CHILDREN_NODE_MAP.put(path, value);

        path = PARENT_PATH + path;

        //创建临时节点
        ZK_CLIENT.createEphemeral(path);
        System.out.println("zk创建节点" + path);
        ZK_CLIENT.writeData(path, value);
        System.out.println("节点" + path + " 写入值 " + value);

        //注册监听,监听子节点
        ZK_CLIENT.subscribeChildChanges(PARENT_PATH, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> list) throws Exception {
                //子节点变化，更新本地存储的节点列表，有少则删除zk上的节点
                Map<String, String> currentChildrenMap = new HashMap<>(16);
                if(list != null && list.size() > 0) {
                    list.forEach(node -> {
                        node = "/" + node;
                        //更新新增的节点
                        CHILDREN_NODE_MAP.putIfAbsent(node, node);
                        currentChildrenMap.put(node, node);
                    });
                }
                //剔除下线的节点 非持久节点断开后会自动删除
            }
        });

        //注册监听
        ZK_CLIENT.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println(s + "该节点内容被更新，更新的内容" + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println(s + "该节点被删除");
            }
        });
    }
}
