package com.lagou.rpc.consumer;

import com.lagou.rpc.consumer.handler.RpcClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 客户端
 * 1、连接Netty服务端
 * 2、提供给调用者主动关闭资源的方法
 * 3、提供消息发送的方法
 */
public class RpcClient {
    private NioEventLoopGroup eventLoopGroup;
    private Channel channel;

    private String ip;
    private int port;

    RpcClientHandler rpcClientHandler = new RpcClientHandler();

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public RpcClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        initClient();
    }

    /**
     * 初始化-连接netty
     */
    public void initClient() {
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(rpcClientHandler);

                    }
                });

        try {
            channel = bootstrap.connect(ip, port).sync().channel();
            System.out.println("==rpc客户端连接成功！...");
        } catch (InterruptedException e) {
            e.printStackTrace();
            close();
        }
    }

    /**
     *  * 2、提供给调用者主动关闭资源的方法
     */
    public void close() {
        if (channel!=null){
            channel.close();
        }
        if (eventLoopGroup!=null){
            eventLoopGroup.shutdownGracefully();
        }

    }

    /**
     *  * 3、提供消息发送的方法
     * @return
     */
    public Object send(String msg) throws ExecutionException, InterruptedException {
        //设置请求信息
        rpcClientHandler.setRequestMsg(msg);
        //执行线程
        Future submit = executorService.submit(rpcClientHandler);
        //返回接收的值
        return submit.get();
    }


}
