package com.lagou.rpc.consumer.discover;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lagou.rpc.consumer.RpcClient;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 服务发现
 */
@Component
public class ServiceDiscovery implements IDiscover {

    @Value("${zk.port}")
    int port;
    @Value("${zk.ip}")
    String ip;

    ZkClient zkClient;
    @Autowired
    NettyRpcConnection nettyRpcConnection;

    private static final String PARENT ="/netty";

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

                list.forEach(hostNode ->{
                    String hostData = readHostData(hostNode);
                    //放入新增节点
                    if (hostMap.get(hostNode)==null){
                        System.out.println("\n>>> 新服务端注册 up up⬆️⬆️⬆️!!!："+hostNode +"->"+ hostData);
                        hostMap.put(hostNode, hostData);
                    }

                    //创建rpc连接
                    hostClientMap.computeIfAbsent(hostNode, nettyRpcConnection::createRpcConnection);
                });

                //移除下线节点
                hostMap.forEach((k,v)->{
                    if (!hostAddress.contains(k)){
                        System.out.println("\n>>>服务端下线 down ⬇️⬇️⬇️，移除："+ k);
                        //清除
                        hostMap.put(k, null);

                        System.out.println("移除rpc连接："+k);
                        //移除连接
                        hostClientMap.remove(k);
                    }
                });
            }
        });

        return null;
    }



    /**
     * 获取注册的服务端列表
     */
    private void getRegistServerList() {
        List<String> children = zkClient.getChildren(PARENT);
        System.out.println("获取服务端列表......");
        if (! CollectionUtils.isEmpty(children)){
            children.forEach(child ->{
                hostMap.put(child, readHostData(child));
            });
        }
    }

    private String readHostData(String child) {
        return zkClient.readData(PARENT+"/" + child).toString();
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
