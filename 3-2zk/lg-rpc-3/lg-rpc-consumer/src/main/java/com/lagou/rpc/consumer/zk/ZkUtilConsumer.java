package com.lagou.rpc.consumer.zk;

import com.lagou.rpc.consumer.connection.NettyConnection;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.data.Stat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ZkUtilConsumer {

    private static ZkClient ZK_CLIENT = null;

    /**
     * 根路径
     */
    private static final String PARENT_PATH = "/netty";

    /**
     * 节点的本地缓存
     */
    private static final Map<String, String> CHILDREN_NODE_MAP = new HashMap<>(16);

    /**
     * 定时任务线程池
     */
    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);


    public ZkUtilConsumer() {
    }

    public ZkClient connect() {

        ZK_CLIENT = new ZkClient("127.0.0.1:2184");

        System.out.println("============= zk连接建立 =============");

        //建立初始节点
        //判断是否存在
        boolean exists = ZK_CLIENT.exists(PARENT_PATH);
        if(!exists) {
            //节点不存在，创建临时节点
            ZK_CLIENT.createEphemeral(PARENT_PATH);
        }

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
                        if(CHILDREN_NODE_MAP.get(node) == null) {
                            //不存在，新增
                            System.out.println("更新新增的节点" + node);
                            CHILDREN_NODE_MAP.put(node, node);
                            currentChildrenMap.put(node, node);
                            //建立连接
                            Object readData = ZK_CLIENT.readData(PARENT_PATH + node);

                            NettyConnection.addConnection(readData.toString());
                            System.out.println("============= 新增的节点"+node+"建立连接成功 =============");
                        }
                    });
                }
                //剔除下线的节点
                CHILDREN_NODE_MAP.forEach((key, value) -> {
                    if(currentChildrenMap.get(key) == null) {
                        //这个节点不存在了,删除
                        CHILDREN_NODE_MAP.put(key, null);
                    }
                });
            }
        });

        /*
        开启定时任务，5s执行获取一次zk的子节点的值，
        检查该节点的最后一次请求的时间与当前时间是否超过5s，
        超过则进行置空数据，没有则不处理
         */
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Map<String, String> hostAndNodeMap = getChildrenHost(true);
                long current = System.currentTimeMillis();
                System.out.println("定时5秒检测，当前时间：" + current);
                hostAndNodeMap.forEach((host, node) -> {
                    String[] arr = host.split("#");
                    //ip#port#time#dealTime
                    String ip = arr[0];
                    String port = arr[1];
                    if(arr.length > 2) {
                        long preTime = Long.parseLong(arr[2]);
                        if(current - preTime > 5000) {
                            ZkUtilConsumer.updateNodeVal(node, ip+"#"+port);
                            System.out.println("============= host:"+host+"超时" + (current - preTime) + "ms，时间置空 =============");
                        }
                    }
                });
            }
        }, 5, 5, TimeUnit.SECONDS);


        return ZK_CLIENT;
    }

    public Map<String, String> getChildrenHost(boolean needTime) {
        Map<String, String> hostAndNodeMap = new HashMap<>(6);
        List<String> children = ZK_CLIENT.getChildren(PARENT_PATH);
        if(children != null && children.size() > 0) {
            children.forEach(child -> {
                child = "/" + child;
                //读取节点内容
                Object readData = ZK_CLIENT.readData(PARENT_PATH + child);
                if(needTime) {
                    hostAndNodeMap.put(readData.toString(), PARENT_PATH + child);
                } else {
                    String host = readData.toString();
                    String[] arr = host.split("#");
                    //ip#port#time
                    String ip = arr[0];
                    String port = arr[1];
                    hostAndNodeMap.put(ip+"#"+port, PARENT_PATH + child);
                }

                //本地储存节点
                CHILDREN_NODE_MAP.putIfAbsent(child, child);
            });
        }

        return hostAndNodeMap;
    }

    static Stat updateNodeVal(String nodePath, String value) {
        if(!ZK_CLIENT.exists(nodePath)) {
            //节点不存在，创建临时节点
            ZK_CLIENT.createEphemeral(nodePath);
        }
        return ZK_CLIENT.writeData(nodePath, value);
    }

}
