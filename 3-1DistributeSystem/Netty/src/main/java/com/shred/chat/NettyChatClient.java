package com.shred.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class NettyChatClient {
    private String ip;
    private int port;

    public void run() throws InterruptedException {
        //1. 创建线程组
        NioEventLoopGroup group = null;

        try {
            group = new NioEventLoopGroup();
            //2. 创建客户端启动助手
            Bootstrap bootstrap = new Bootstrap();
            //3. 设置线程组
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel
                            >() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            //4. 设置客户端通道实现为NIO
            //5. 创建一个通道初始化对象
            //6. 向pipeline中添加自定义业务处理handler
            //7. 启动客户端,等待连接服务端,同时将异步改为同步
            ChannelFuture future = bootstrap.connect(ip, port).sync();
            Channel channel = future.channel();
            System.out.println("======" + channel.localAddress().toString().substring(1) + "-=====");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                channel.writeAndFlush(s);
            }
            //8. 关闭通道和关闭连接池
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }


    public NettyChatClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyChatClient("127.0.0.1", 9998).run();
    }
}
