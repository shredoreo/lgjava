package com.lagou.rpc.consumer.proxy;

import com.alibaba.fastjson.JSON;
import com.lagou.rpc.common.RpcRequest;
import com.lagou.rpc.common.RpcResponse;
import com.lagou.rpc.consumer.RpcClient;
import com.lagou.rpc.consumer.discover.ServiceDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

@Component
public class RpcClientProxy {

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

                        String discoverAddress = serviceDiscovery.discover(serviceClass.getName());
                        System.out.println("获取的服务端地址：" + discoverAddress);
                        String[] split = discoverAddress.split(":");

                        //创建rpc客户端
                        RpcClient rpcClient = new RpcClient(split[0], Integer.parseInt(split[1]));

                        try {
                            Object message = rpcClient.send(JSON.toJSONString(rpcRequest));
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
                        } finally {
                            rpcClient.close();
                        }
                    }
                }
        );

    }
}
