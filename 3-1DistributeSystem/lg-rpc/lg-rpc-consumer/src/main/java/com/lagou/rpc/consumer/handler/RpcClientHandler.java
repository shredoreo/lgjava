package com.lagou.rpc.consumer.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Callable;

/**
 * 客户端处理类
 * 1、发送消息
 * 2、接收消息
 */
// todo Callable
//使用线程的等待唤醒机制
public class RpcClientHandler extends SimpleChannelInboundHandler<String> implements Callable {
    //就绪时赋值
    ChannelHandlerContext ctx;

    //发送 的消息
    String requestMsg;

    //服务端的消息
    String responseMsg;

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    /**
     * 通道读取就绪
     *
     * @param channelHandlerContext
     * @param s
     * @throws Exception
     */
    @Override
    protected synchronized void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        responseMsg = s;
        //唤醒等待的线程
        notify();
    }

    /**
     * 通道就绪事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;

    }

    /**
     * 发送消息到服务端
     *
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        //发消息
        ctx.writeAndFlush(requestMsg);
        //线程 等待
        wait();

        return responseMsg;
    }
}
