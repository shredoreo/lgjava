package com.shred.unpack.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * Netty服务端
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //1. 创建bossGroup线程组: 处理网络事件--连接事件
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);

        //2. 创建workerGroup线程组: 处理网络事件--读写事件，未设置线程数，默认为2*处理器的线程数
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //3. 创建服务端启动助手
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //4. 设置bossGroup线程组和workerGroup线程组
        serverBootstrap.group(bossGroup, workerGroup)
                //5. 设置服务端通道实现为NIO
                .channel(NioServerSocketChannel.class)
                //6. 参数设置
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .childHandler(
                        //7. 创建一个通道初始化对象
                        new ChannelInitializer<SocketChannel>() {//传入泛型
                            @Override
                            protected void initChannel(SocketChannel channel) throws Exception {
                                channel.pipeline().addLast(new LineBasedFrameDecoder(2048));//

                                //8. 向pipeline中添加自定义业务处理handler
                                channel.pipeline().addLast(new NettyServerHandler());
                            }
                        })
        ;

        //9. 启动服务端并绑定端口,同时将异步改为同步
        ChannelFuture future = serverBootstrap.bind(9999).sync();
        System.out.println("服务端启动成功");

        //10. 关闭通道（并非真正意义的关闭，而是监听通道的关闭状态）和关闭连接池
        future.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

    }


}
