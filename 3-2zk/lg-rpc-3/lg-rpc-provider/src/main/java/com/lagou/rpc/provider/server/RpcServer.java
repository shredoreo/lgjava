package com.lagou.rpc.provider.server;

import com.lagou.rpc.provider.handler.RpcServerHandler;
import com.lagou.rpc.provider.register.RegisterCenter;
import com.lagou.rpc.provider.zk.ServerRegistry;
import com.lagou.rpc.provider.zk.ZkUtil1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 启动类
 */
@Service
public class RpcServer implements DisposableBean {

    @Autowired
    RpcServerHandler rpcServerHandler;

    @Autowired
    RegisterCenter registerCenter;

    @Autowired
    ServerRegistry  serverRegistry;

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;

    public void startServer(String ip, int port) {
        try {

            //创建线程俎
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        //
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(rpcServerHandler);
                    }
                });

            ChannelFuture sync = serverBootstrap.bind(ip, port).sync();

            String address = ip + ":" + port;
            //服务注册 serviceName -> ip:port
         /*   rpcServerHandler.SERVICE_INSTANCE_MAP.keySet().forEach(
                    k -> {
                        registerCenter.register(k, address);
                    });

            // 服务注册 直接注册当前服务的地址
            serverRegistry.register(ip +":"+port);*/

            String data = ip + "#" + port;

            //注册服务
            new ZkUtil1().createNode("/"+data, data);

            System.out.println("=====服务端启动成功=====");
            //监听服务端关闭
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
         } finally {
            close();
        }
    }

    @Override
    public void destroy() throws Exception {

        close();
    }

    private void close() {
        if (bossGroup!=null){
            bossGroup.shutdownGracefully();
        }
        if (workerGroup!=null){
            workerGroup.shutdownGracefully();
        }
    }
}
