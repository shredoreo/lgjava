package com.lagou.rpc.provider.register;

import com.lagou.rpc.provider.handler.RegisterHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RegisterServer implements Runnable{

    @Value("${registry.ip}")
    private String host;
    @Value("${registry.port}")
    private int port;
    private NioEventLoopGroup bossGroup;

    @Autowired
    RegisterHandler registerHandler;

    @Override
    public void run() {
        bossGroup = new NioEventLoopGroup(1);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        //注册处理器
                        pipeline.addLast(registerHandler);
                    }
                });
        try {
            //启动监听
            ChannelFuture sync = serverBootstrap.bind(host, port).sync();
            System.out.println("注册服务启动成功");

            //服务端关闭
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("注册服务启动出错");
        }
    }
}
