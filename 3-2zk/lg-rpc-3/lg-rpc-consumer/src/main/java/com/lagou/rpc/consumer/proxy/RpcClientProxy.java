package com.lagou.rpc.consumer.proxy;

import com.alibaba.fastjson.JSON;
import com.lagou.rpc.common.RpcRequest;
import com.lagou.rpc.common.RpcResponse;
import com.lagou.rpc.constant.Const;
import com.lagou.rpc.consumer.RpcClient;
import com.lagou.rpc.consumer.connection.NettyRpcConnection;
import com.lagou.rpc.consumer.discover.ServiceDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.UUID;

@Component
public class RpcClientProxy {
    @Autowired
    NettyRpcConnection nettyRpcConnection;

    @Autowired
    ServiceDiscovery serviceDiscovery;

    public Object createProxy(Class<?> serviceClass) {
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        RpcRequest rpcRequest = new RpcRequest();
                        rpcRequest.setRequestId(UUID.randomUUID().toString());
                        rpcRequest.setClassName(method.getDeclaringClass().getName());
                        rpcRequest.setMethodName(method.getName());
                        rpcRequest.setParameters(args);
                        rpcRequest.setParameterTypes(method.getParameterTypes());

                        //负载均衡
                        String hostAddress = serviceDiscovery.loadBalanceByCostTime();
                        HashMap<String, RpcClient> hostClientMap = nettyRpcConnection.getHostClientMap();

                        RpcClient rpcClient = hostClientMap.get(hostAddress);

                        try {
                            long start = System.currentTimeMillis();
                            Object message = rpcClient.send(JSON.toJSONString(rpcRequest));
                            long end = System.currentTimeMillis();
                            //计算耗时
                            long cost = end - start;

                            System.out.println(">>>>> 本次请求"+hostAddress +" >>> 本次耗时："+ cost);
                            System.out.println();
                            //更新服务端请求事件
                            serviceDiscovery.updateHostData(hostAddress,"" +end+ Const.DELIMIT+ cost );

                            RpcResponse rpcResponse = JSON.parseObject(message.toString(), RpcResponse.class);
                            if (rpcResponse.getError() != null) {
                                throw new RuntimeException(rpcResponse.getError());
                            }

                            //返回结果
                            Object result = rpcResponse.getResult();
                            return JSON.parseObject(result.toString(), method.getReturnType());
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw e;
                        }/* finally {
                            rpcClient.close();
                        }*/
                    }
                }
        );

    }
}
