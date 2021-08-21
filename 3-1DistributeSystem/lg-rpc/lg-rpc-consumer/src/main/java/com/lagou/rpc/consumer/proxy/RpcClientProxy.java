package com.lagou.rpc.consumer.proxy;

import com.alibaba.fastjson.JSON;
import com.lagou.rpc.common.RpcRequest;
import com.lagou.rpc.common.RpcResponse;
import com.lagou.rpc.consumer.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RpcClientProxy {

    public static Object createProxy(Class<?> serviceClass) {
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
                        //创建rpc客户端
                        RpcClient rpcClient = new RpcClient("127.0.0.1", 8898);
                        try{
                            Object message = rpcClient.send(JSON.toJSONString(rpcRequest));
                            RpcResponse rpcResponse = JSON.parseObject(message.toString(), RpcResponse.class);
                            if (rpcResponse.getError()!=null){
                                throw new RuntimeException(rpcResponse.getError());
                            }

                            //返回结果
                            Object result = rpcResponse.getResult();
                            return JSON.parseObject(result.toString(), method.getReturnType());
                        } catch (Exception e){
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
