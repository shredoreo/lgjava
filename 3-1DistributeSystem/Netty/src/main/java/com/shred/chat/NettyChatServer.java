package com.shred.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyChatServer {

    private int port;

    public void run() throws InterruptedException {
        NioEventLoopGroup bossGroup = null;
        NioEventLoopGroup workerGroup = null;
        try {

            //1. 创建bossGroup线程组: 处理网络事件--连接事件
            bossGroup = new NioEventLoopGroup();
            //2. 创建workerGroup线程组: 处理网络事件--读写事件
            workerGroup = new NioEventLoopGroup();

            //3. 创建服务端启动助手
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            //4. 设置bossGroup线程组和workerGroup线程组
            //5. 设置服务端通道实现为NIO
            //6. 参数设置
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                    .childHandler(
                            //7. 创建一个通道初始化对象
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline().addLast(new StringDecoder());
                                    ch.pipeline().addLast(new StringEncoder());
                                    //8. 向pipeline中添加自定义业务处理handler
                                    ch.pipeline().addLast(new NettyChatServerHandler());

                                }
                            });

            //9. 启动服务端并绑定端口,同时将异步改为同步
            ChannelFuture channelFuture = serverBootstrap.bind(port);
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("端口绑定成功！");
                    } else {
                        System.out.println("端口绑定失败！");
                    }
                }
            });
            System.out.println("服务器启动成功！");
            //10. 关闭通道和关闭连接池
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public NettyChatServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyChatServer(9998).run();
    }
}
