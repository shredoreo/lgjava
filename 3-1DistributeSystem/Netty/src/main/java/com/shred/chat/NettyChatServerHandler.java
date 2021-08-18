package com.shred.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static List<Channel> channelList = new ArrayList<Channel>();

    /**
     * 通道就绪事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelList.add(channel);
        System.out.println("[Server]:" + channel.remoteAddress().toString().substring(1) +"在线");

    }

    /**
     * 下线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        channelList.remove(channel);
        System.out.println("[Server]:" + channel.remoteAddress().toString().substring(1) +"下线");
    }


    /**
     * 通道读取就绪
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        for (Channel channel1 : channelList) {
            if (channel!= channel1){
                //向客户端写数据
                channel1.writeAndFlush("["+
                        channel.remoteAddress().toString().substring(1) + "]说了："+msg);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        Channel channel = ctx.channel();
        System.out.println("[Server]:"+ channel.remoteAddress().toString().substring(1) +"异常。");

    }
}
