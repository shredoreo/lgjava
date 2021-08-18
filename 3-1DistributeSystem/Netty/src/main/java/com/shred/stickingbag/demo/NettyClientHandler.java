package com.shred.stickingbag.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.Arrays;

/**
 * 客户端处理
 */
public class NettyClientHandler implements ChannelInboundHandler {
    /**
     * 通道就绪
     * @param channelHandlerContext
     * @throws Exception
     */
    @Override
    public void channelActive( ChannelHandlerContext channelHandlerContext) throws Exception {
        //一次发送102400字节数据
        int len = 102400;
        char[] chars = new char[len];
        Arrays.fill(chars, 'a');
        chars[len-1] = '\n';
        for (int i = 0; i < 10; i++) {
            channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(chars, CharsetUtil.UTF_8));
        }
    }

    /**
     * 通道读就绪
     * @param channelHandlerContext
     * @param o
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ByteBuf byteBuf = (ByteBuf) o;
        System.out.println("服务端发送的消息："+ byteBuf.toString(CharsetUtil.UTF_8));

    }
    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }



    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }


    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

    }
}
