package com.lagou.rpc.provider.handler;

import com.alibaba.fastjson.JSONObject;
import com.lagou.rpc.provider.register.RegisterCenter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ChannelHandler.Sharable
public class RegisterHandler extends SimpleChannelInboundHandler<String> {

    @Autowired
    RegisterCenter registerCenter;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }

    /**
     * 通道 就绪
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        SocketAddress socketAddress1 = channel.localAddress();
        System.out.println(socketAddress);
        System.out.println(socketAddress1);

        //将注册的服务推送给客户端
        ConcurrentHashMap<String, List<String>> serviceRegistry = registerCenter.serviceRegistry;

        String s = JSONObject.toJSONString(serviceRegistry);
        System.out.println(s);
        channel.writeAndFlush(s);

    }
}
