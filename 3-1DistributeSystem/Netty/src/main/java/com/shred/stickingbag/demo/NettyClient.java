package com.shred.stickingbag.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.nio.charset.StandardCharsets;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //1. 创建线程组
        NioEventLoopGroup group = new NioEventLoopGroup();

        //2. 创建客户端启动助手
        Bootstrap bootstrap = new Bootstrap();
        //3. 设置线程组
        bootstrap.group(group)
                //4. 设置客户端通道实现为NIO
                .channel(NioSocketChannel.class)
                .handler(
                        //5. 创建一个通道初始化对象
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(new LineBasedFrameDecoder(
                                        102400
                                ));
                                //6. 向pipeline中添加自定义业务处理handler
                                socketChannel.pipeline().addLast(new NettyClientHandler());
                            }
                        });


        //7. 启动客户端,等待连接服务端,同时将异步改为同步
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9999).sync();
        //8. 关闭通道和关闭连接池
        channelFuture.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
