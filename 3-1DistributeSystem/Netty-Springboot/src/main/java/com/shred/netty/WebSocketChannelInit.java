package com.shred.netty;

import com.shred.config.NettyConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketChannelInit extends ChannelInitializer
{

    @Autowired
    NettyConfig nettyConfig;

    @Autowired
    WebSocketHandler webSocketHandler;

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //http协议支持
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());

        pipeline.addLast(new HttpObjectAggregator(8000));

        //webSocket处理
        pipeline.addLast(new WebSocketServerProtocolHandler(nettyConfig.getPath()));

        pipeline.addLast(webSocketHandler);
    }
}
