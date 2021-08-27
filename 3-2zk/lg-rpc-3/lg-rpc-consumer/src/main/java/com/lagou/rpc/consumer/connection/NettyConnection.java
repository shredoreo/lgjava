package com.lagou.rpc.consumer.connection;

import com.lagou.rpc.api.IUserService;
import com.lagou.rpc.common.RpcResponse;
import com.lagou.rpc.consumer.zk.ZkUtilConsumer;

import java.util.*;

public class NettyConnection {

    /**
     * 参数定义
     */
    private static final String PROVIDER_NAME = "UserService#sayHello#";

    /**
     * 连接
     */
    private static final Map<String, IUserService> USER_SERVICE_MAP = new HashMap<>(6);


    public static void createConnection(Map<String, String> hostAndNodeMap) {

        List<String> childrenHost = new ArrayList<>();
        hostAndNodeMap.forEach((host, node) -> childrenHost.add(host));
        //初始化连接
        RpcConsumer.clientBuild(childrenHost);

        if(childrenHost.size() > 0) {
            childrenHost.forEach(childHost -> {
                //1.创建代理对象
                IUserService userService = (IUserService) RpcConsumer.createProxy(IUserService.class, PROVIDER_NAME, childHost);
                USER_SERVICE_MAP.put(childHost, userService);
            });

            //2.循环给服务器写数据
            while (true) {
                if(USER_SERVICE_MAP.size() > 0) {
                    //任务一：连接2个客服端，向服务端发送消息
                    USER_SERVICE_MAP.forEach((host, userService) -> doNetty(host, userService, hostAndNodeMap));

                    //任务二，每次都选择最后一次响应时间短的服务端进行服务调用，如果时间一致，随机选取一个服务端进行调用，从而实现负载均衡
//                    doLoadBalance(userServiceMap, hostAndNodeMap);

                    //睡2s方便查看输出
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 任务二，每次都选择最后一次响应时间短的服务端进行服务调用，如果时间一致，随机选取一个服务端进行调用，从而实现负载均衡
     */
    private static void doLoadBalance(Map<String, IUserService> userServiceMap, Map<String, String> hostAndNodeMap) {
        Map<String, String> hostAndNodeMapWithTimeMap = new ZkUtilConsumer().getChildrenHost(true);
        //获取时间响应时间最短的,优先没有时间数据的
        if(hostAndNodeMapWithTimeMap.size() > 0) {
            String targetHost = null;
            long preDealTime = -1;
            for(Map.Entry<String, String> entry : hostAndNodeMapWithTimeMap.entrySet()) {
                String host = entry.getKey();

                String[] arr = host.split("#");
                //ip#port#time#dealTime
                String ip = arr[0];
                String port = arr[1];

                //延迟检测，服务端停止，zk上对应的node失效会有延迟性
                if(userServiceMap.get(ip+"#"+port) != null) {
                    //没有时间数据的，优先
                    if(arr.length > 2) {
                        long dealTime = Long.parseLong(arr[3]);
                        if(preDealTime == -1 || dealTime < preDealTime) {
                            targetHost = ip + "#" + port;
                            preDealTime = dealTime;
                        }
                    } else {
                        targetHost = ip + "#" + port;
                        preDealTime = 0;
                        break;
                    }
                }
            }

            System.out.println("选择响应时间最短的：host -> " + targetHost + "#" + preDealTime);

            doNetty(targetHost, userServiceMap.get(targetHost), hostAndNodeMap);
        }
    }

    /**
     * 任务一：连接2个客服端，向服务端发送消息
     */
    private static void doNetty(String host, IUserService userService, Map<String, String> hostAndNodeMap) {
        //断开的会被更新为null，所以这里要加个判断
        if(userService != null) {
            long start = System.currentTimeMillis();
            System.out.println("客户端开始");
            RpcResponse result = userService.sayHello("Hi I am Tom, I want to play a game with u !");
            System.out.println("客服端返回：" + result.toString());
            System.out.println("客户端结束");

            //人为自造5S空挡
            Random rd = new Random();
            int i = rd.nextInt(5);
            try {
                Thread.sleep(i*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long end = System.currentTimeMillis();

            long dealTime = end - start;
            System.out.println(host+": 耗时：" + dealTime + "ms");

            //节点值绑定时间和耗时
            ZkUtilConsumer.updateNodeVal(hostAndNodeMap.get(host), host+"#"+end+"#"+dealTime);
            System.out.println("更新节点值时间：" + host+"#"+end+"#"+dealTime);
        }
    }

    /**
     * 链接断开时移除本地连接
     */
    public static synchronized void removeConnection(String host) {
        //用于连接
        USER_SERVICE_MAP.put(host, null);
    }

    /**
     * 新增连接
     */
    public static synchronized void addConnection(String host) {
        String[] arr = host.split("#");
        //ip#port#time#dealTime
        String ip = arr[0];
        String port = arr[1];
        host = ip + "#" + port;

        //初始化连接
        List<String> childrenHost = new ArrayList<>();
        childrenHost.add(host);
        RpcConsumer.clientBuild(childrenHost);
        IUserService userService = (IUserService) RpcConsumer.createProxy(IUserService.class, PROVIDER_NAME, host);
        //用于连接
        USER_SERVICE_MAP.put(host, userService);
    }

}
