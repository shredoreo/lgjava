package com.lagou.rpc.consumer.connection;

import com.lagou.rpc.common.RpcRequest;
import com.lagou.rpc.common.RpcResponse;
import com.lagou.rpc.consumer.handler.UserClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    /**
     * 2.声明一个自定义事件处理器  UserClientHandler
     */
    private final UserClientHandler userClientHandler;


    private final String ip;

    private final int port;

    Client(String ip, int port, UserClientHandler userClientHandler) {
        this.ip = ip;
        this.port = port;
        this.userClientHandler = userClientHandler;
    }

    void initClient() throws InterruptedException {
        //1)创建连接池对象
        NioEventLoopGroup group = new NioEventLoopGroup();
        //2)创建客户端的引导对象
        Bootstrap bootstrap = new Bootstrap();
        //3)配置启动引导对象
        bootstrap.group(group)
                //设置通道为NIO
                .channel(NioSocketChannel.class)
                //设置请求协议为TCP
                .option(ChannelOption.TCP_NODELAY, true)
                //监听channel 并初始化
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //获取ChannelPipeline
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //设置编码
                        pipeline.addLast(new RpcEncoder(RpcRequest.class, new JSONSerializer()));
                        pipeline.addLast(new RpcDecoder(RpcResponse.class, new JSONSerializer()));

                        //添加自定义事件处理器
                        pipeline.addLast(userClientHandler);
                    }
                });

        bootstrap.connect(ip, port).sync();
    }

    UserClientHandler getUserClientHandler() {
        return userClientHandler;
    }

    String getInfo() {
        return "UserClientHandler: ip-> "+this.ip + " port-> " + this.port;
    }

}
