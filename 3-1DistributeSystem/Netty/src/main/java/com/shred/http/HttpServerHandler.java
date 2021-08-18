package com.shred.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * 读取就绪事件
     * @param channelHandlerContext
     * @param httpObject
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        //判断本次是不是http请求
        if (httpObject instanceof HttpRequest){
            DefaultHttpRequest request = (DefaultHttpRequest) httpObject;
            System.out.println("浏览器请求路径："+request.uri());
            if ("/favicon.ico".equals(request.uri())) {
                System.out.println("图标不响应");
                return; }
            ByteBuf bf = Unpooled.copiedBuffer("Hello 我的netty服务器", CharsetUtil.UTF_8);
            DefaultFullHttpResponse defaultHttpResponse = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    bf
            );
            //响应头
            defaultHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
            defaultHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, bf.readableBytes());

            //写入
            channelHandlerContext.writeAndFlush(defaultHttpResponse);
        }
    }
}
