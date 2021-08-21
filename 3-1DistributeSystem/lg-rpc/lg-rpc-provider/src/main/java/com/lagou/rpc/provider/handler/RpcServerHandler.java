package com.lagou.rpc.provider.handler;

import com.alibaba.fastjson.JSON;
import com.lagou.rpc.common.RpcRequest;
import com.lagou.rpc.common.RpcResponse;
import com.lagou.rpc.provider.anno.RpcService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端业务处理类
 * 1、将@RptService端bean缓存
 * 2、接收客户端请求
 * 3、根据beanName从缓存查找bean
 * 4、解析请求中的方法名称、参数类型、信息
 * 5、给客户端进行响应
 */
@Component
@ChannelHandler.Sharable//共享
public class RpcServerHandler extends SimpleChannelInboundHandler<String> implements ApplicationContextAware {

    private ConcurrentHashMap<String, Object> SERVICE_INSTANCE_MAP = new ConcurrentHashMap<String, Object>();

    /**
     * 通道读取就绪事件
     *
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        //1、接收客户端请求 msg转成RpcRequest
        RpcRequest rpcRequest = JSON.parseObject(msg, RpcRequest.class);

        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());

        try {
            //业务处理
            response.setResult(handler(rpcRequest));
        } catch (Exception e) {
            e.printStackTrace();
            response.setError(e.getMessage());

        }

        channelHandlerContext.writeAndFlush(JSON.toJSONString(response));
    }

    /**
     * 业务处理逻辑
     *
     * @return
     */
    public Object handler(RpcRequest rpcRequest) throws InvocationTargetException {
        // * 3、根据beanName从缓存查找bean
        Object serviceBean = SERVICE_INSTANCE_MAP.get(rpcRequest.getClassName());
        if (serviceBean == null){
            throw new RuntimeException("根据beanName找不到服务 beanName:" +rpcRequest.getClassName());
        }

        // * 4、解析请求中的方法名称、参数类型、信息
        Class<?> serviceBeanClass = serviceBean.getClass();
        String methodName = rpcRequest.getMethodName();
        Object[] parameters = rpcRequest.getParameters();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();

        // * 5、给客户端进行响应
        //通过cglib创建对象，并调用
        FastClass fastClass = FastClass.create(serviceBeanClass);
        FastMethod method = fastClass.getMethod(methodName, parameterTypes);
        return method.invoke(serviceBean, parameters);
    }

    /**
     * 用于
     * 1、将@RptService端bean缓存
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取容器中标注了 RpcService 接口的bean
        Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (!CollectionUtils.isEmpty(serviceMap)) {
            Set<Map.Entry<String, Object>> entries = serviceMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                Object serviceBean = entry.getValue();
                if (serviceBean.getClass().getInterfaces().length == 0) {
                    throw new RuntimeException("服务必须实现接口");
                }
                //默认取第一个接口作为名称，即缓存bean的名称，暴露出去
                String name = serviceBean.getClass().getInterfaces()[0].getName();
                SERVICE_INSTANCE_MAP.put(name, serviceBean);

            }
        }
    }
}
