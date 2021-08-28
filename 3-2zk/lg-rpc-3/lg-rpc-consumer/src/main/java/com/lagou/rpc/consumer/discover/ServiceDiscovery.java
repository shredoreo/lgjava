package com.lagou.rpc.consumer.discover;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lagou.rpc.constant.Const;
import com.lagou.rpc.consumer.RpcClient;
import com.lagou.rpc.consumer.connection.NettyRpcConnection;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 服务发现
 */
@Component
public class ServiceDiscovery implements IDiscover {

    @Value("${zk.port}")
    int port;
    @Value("${zk.ip}")
    String ip;

    @Value("${ex.delay}")
    int delay;

    ZkClient zkClient;
    @Autowired
    NettyRpcConnection nettyRpcConnection;

    /**
     * 定时任务线程池
     */
    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);


    private static final String PARENT = "/netty";

    int count;

    JSONObject serviceMap;

    HashMap<String, String> hostMap = new HashMap<>();

    public JSONObject getServiceMap() {
        return serviceMap;
    }

    public void setServiceMap(JSONObject serviceMap) {
        this.serviceMap = serviceMap;
    }


    /**
     * 连接zk，获取服务端列表，建立rpc连接
     *
     * @return
     */
    @Override
    public ZkClient connect() {
        zkClient = new ZkClient(ip, port);
        if (!zkClient.exists(PARENT)) {
            zkClient.createPersistent(PARENT);
        }
        System.out.println(">>> zk已连接\n");
        //获取服务端列表，保存再hostMap中
        getRegistServerList();

        //准备rpc连接
        nettyRpcConnection.prepareNettyRpcConn(hostMap);

        zkClient.subscribeChildChanges(PARENT, new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println("\n节点发生变化.....");
                HashMap<String, RpcClient> hostClientMap = nettyRpcConnection.getHostClientMap();

                HashSet<String> hostAddress = new HashSet<>(list);

                list.forEach(hostNode -> {
                    String hostData = readHostData(hostNode);
                    //放入新增节点
                    if (hostMap.get(hostNode) == null) {
                        System.out.println("\n>>> 新服务端注册 up up⬆️⬆️⬆️!!!：" + hostNode + "->" + hostData);
                        hostMap.put(hostNode, hostData);
                    }

                    //创建rpc连接
                    hostClientMap.computeIfAbsent(hostNode, nettyRpcConnection::createRpcConnection);
                });

                //移除下线节点
                hostMap.forEach((k, v) -> {
                    if (!hostAddress.contains(k)) {
                        System.out.println("\n>>>服务端下线 down ⬇️⬇️⬇️，移除：" + k);
                        //清除
                        hostMap.put(k, null);

                        System.out.println("移除rpc连接：" + k);
                        //移除连接
                        RpcClient rpcClient = hostClientMap.get(k);
                        rpcClient.close();
                        hostClientMap.remove(k);
                    }
                });
            }
        });

        //开始定时任务
        startSchedule();

        return null;
    }

    /**
     * 开启定时任务
     * 定时上报
     */
    private void startSchedule() {

        //5s执行一次
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                long current = System.currentTimeMillis();
                System.out.println();
                System.out.println("----定时检测，当前时间：" + new Date(current));

                List<String> children = zkClient.getChildren(PARENT);
                children.forEach(host->{
                    String val = readHostData(host);
                    String[] arr = val.split(Const.DELIMIT);
                    //timestamp#cost
                    long lastRequestTime = Long.parseLong(arr[0]);
                    System.out.println(host+ "-----定时上报："+ val);

                    if (arr.length>1){
                        long cost = Long.parseLong(arr[1]);
                        System.out.println("\t"+ "-----====请求耗时: "+cost);

                        //超过5秒，清除时间
                        if(current - lastRequestTime > delay*1000 ){
                            System.out.println("\t"+"-----!!!超时，更新： "+host);
                            updateHostData(host, current);
                        }

                    }
                });
            }
        }, delay, delay, TimeUnit.SECONDS);

    }

    /***
     * 使用最近请求时间做负载均衡
     * @return
     */
    public String loadBalanceByCostTime() {
        List<String> children = zkClient.getChildren(PARENT);
        Long min = null;
        // 按耗时 分类的服务端地址，
        // 因为可能存在相同的耗时，相同时，要随机抽取一个
        Map<Long, List<String>> hostByCost = new HashMap<>();

        for (String child : children) {
            //节点值就是耗时
            String lastCostTime = readHostData(child);
            String[] split = lastCostTime.split(Const.DELIMIT);
            //timestamp#cost
            Long cost;
            if (split.length>1){
                cost = Long.parseLong(split[1]);
            } else {
                cost = 0L;
            }

            //计算最短耗时
            min = min == null ? cost : Math.min(min, cost);
            //按耗时添加
            hostByCost.computeIfAbsent(cost, k ->new ArrayList<>()).add(child);
        }

        List<String> fastHostList = hostByCost.get(min);

        String hostAddr = fastHostList.get(new Random().nextInt(fastHostList.size()));
        String tag ="> [负载均衡]";
        System.out.println(tag +"上次的最短耗时："+ min);
        System.out.println(tag+ "负载均衡获取的服务端："+hostAddr);
        return hostAddr;
    }


    /**
     * 获取注册的服务端列表
     */
    private void getRegistServerList() {
        List<String> children = zkClient.getChildren(PARENT);
        System.out.println("获取服务端列表......");
        if (!CollectionUtils.isEmpty(children)) {
            children.forEach(child -> {
                hostMap.put(child, readHostData(child));
            });
        }
    }

    /**
     * 读取主机节点的值
     *
     * @param child
     * @return
     */
    private String readHostData(String child) {
        return zkClient.readData(PARENT + "/" + child).toString();
    }

    /**
     * 更新主机节点端值
     *
     * @param hostName
     * @param value
     * @return
     */
    public Stat updateHostData(String hostName, Object value) {
        Stat stat = zkClient.writeData(PARENT + "/" + hostName, value);
        return stat;
    }

    @Override
    public String discover(String serviceName) {
        System.out.println("查找服务:" + serviceName);

        return loadBalance(serviceName);
    }


    /**
     * 负载均衡
     *
     * @param serviceName
     * @return
     */
    private String loadBalance(String serviceName) {

        JSONArray addressArr = serviceMap.getJSONArray(serviceName);

        //简单的 负载均衡算法
        int i = count++ % addressArr.size();

        System.out.println("负载均衡：" + i);

        return addressArr.getString(i);
    }

}
