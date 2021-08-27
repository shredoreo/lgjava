package com.lagou.rpc.consumer.handler;

import com.lagou.rpc.common.RpcRequest;
import com.lagou.rpc.common.RpcResponse;
import com.lagou.rpc.consumer.connection.NettyConnection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

public class UserClientHandler extends SimpleChannelInboundHandler<String> implements Callable
{
    /**
     * 1.定义成员变量
     * context: 事件处理器上下文对象 (存储handler信息,写操作)
     * result: 记录服务器返回的数据
     * param: 记录将要返送给服务器的数据
     */
    private ChannelHandlerContext context;
    private RpcRequest param;
    private RpcResponse result;

    /**
     * 2.实现channelActive  客户端和服务器连接时,该方法就自动执行
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    /**
     * 3.实现channelRead 当我们读到服务器数据,该方法自动执行
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = (RpcResponse) msg;
        notify();
    }

    /**
     * 4.将客户端的数写到服务器
     */
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(param);
        wait();
        return result;
    }

    /**
     * 5.设置参数的方法
     */
    public void setParam(RpcRequest param) {
        this.param = param;
    }
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
