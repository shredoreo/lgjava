package com.shred.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ChannelHandler.Sharable//component是单例的，所以设置为共享通道
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static List<Channel> channelList = new ArrayList<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelList.add(channel);
        System.out.println("有新的连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelList.remove(channel);
        System.out.println("有连接断开");
    }

    /**
     * 读就绪
     * @param channelHandlerContext
     * @param textWebSocketFrame
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();

        Channel channel = channelHandlerContext.channel();
        for (Channel channel1 : channelList) {
            if (channel!= channel1){
                //webSocket消息
                channel1.writeAndFlush(new TextWebSocketFrame(text));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        channelList.remove(ctx.channel());
    }
}
