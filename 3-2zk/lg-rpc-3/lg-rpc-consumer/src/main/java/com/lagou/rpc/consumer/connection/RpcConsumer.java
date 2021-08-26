package com.lagou.rpc.consumer.connection;

import com.lagou.rpc.common.RpcRequest;
import com.lagou.rpc.consumer.handler.UserClientHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcConsumer {

    /**
     * 1.创建一个线程池对象  -- 它要处理我们自定义事件
     */
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * 存储host和client的关联
     */
    private static final Map<String, Client> HOST_AND_CLIENT_MAP = new HashMap<>(6);

    public static void clientBuild(List<String> childrenHost) {

        if(childrenHost.size() > 0) {
            childrenHost.forEach(childHost -> {
                String[] arr = childHost.split("#");
                //ip#port#time
                String ip = arr[0];
                String port = arr[1];
                System.out.println("开始建立连接 ip:"+ip+" port:"+port);
                UserClientHandler userClientHandler = new UserClientHandler();
                Client client = new Client(ip, Integer.parseInt(port), userClientHandler);
                try {
                    client.initClient();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HOST_AND_CLIENT_MAP.put(childHost, client);
            });
        }
    }

    /**
     * 4.编写一个方法,使用JDK的动态代理创建对象
     * serviceClass 接口类型,根据哪个接口生成子类代理对象;   providerParam :  "UserService#sayHello#"
     */
    public static Object createProxy(Class<?> serviceClass, final String providerParam, String childHost) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{serviceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //1)初始化客户端client
                Client client = HOST_AND_CLIENT_MAP.get(childHost);
                System.out.println("HOST:"+childHost + " " + client.getInfo());
                UserClientHandler userClientHandler = client.getUserClientHandler();

                //2)给UserClientHandler 设置param参数

                //修改为RpcRequest
                RpcRequest rpcRequest = new RpcRequest();
                rpcRequest.setRequestId(UUID.randomUUID().toString());
                String[] classNameAndMethod = providerParam.split("#");
                rpcRequest.setClassName(serviceClass.getName());
                rpcRequest.setMethodName(classNameAndMethod[1]);
                rpcRequest.setParameters(args);
                rpcRequest.setParameterTypes(new Class[]{String.class});
                userClientHandler.setParam(rpcRequest);

                //3).使用线程池,开启一个线程处理处理call() 写操作,并返回结果
                //4)return 结果
                return EXECUTOR_SERVICE.submit(userClientHandler).get();
            }
        });
    }
}
