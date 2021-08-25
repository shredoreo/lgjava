package com.lagou.rpc.consumer.handler;

import com.alibaba.fastjson.JSONObject;
import com.lagou.rpc.consumer.discover.ServiceDiscovery;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscoveryHandler extends SimpleChannelInboundHandler<String> {
    JSONObject serviceMap;

    @Autowired
    ServiceDiscovery serviceDiscovery;

    /**
     * 读取到服务端
     * @param channelHandlerContext
     * @param s
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(s);
        System.out.println("====收到服务端的服务列表===");
        System.out.println(jsonObject);

        //写入到服务列表中
        serviceDiscovery.setServiceMap(jsonObject);
        this.serviceMap = jsonObject;
    }


}
