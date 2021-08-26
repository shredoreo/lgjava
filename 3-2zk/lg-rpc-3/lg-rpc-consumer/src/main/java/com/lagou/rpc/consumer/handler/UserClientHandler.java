package com.lagou.rpc.consumer.handler;

import com.lagou.rpc.consumer.connection.NettyConnection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

public class UserClientHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 断开连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        int port = ipSocket.getPort();
        String ip = ipSocket.getHostString();
        System.out.println("============= 与设备"+ip+":"+port+"连接断开! =============");
        final EventLoop eventLoop = ctx.channel().eventLoop();
        //移除本地存储的连接
        NettyConnection.removeConnection(ip+"#"+port);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }
}
