package com.shred.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.CharsetUtil;

import java.util.List;

public class MessageCodec extends MessageToMessageCodec {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
        String msg = (String) o;
        System.out.println("进行消息编码....");
        list.add(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));//传递到下一个handler
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
        System.out.println("正在解码");
        ByteBuf o1 = (ByteBuf) o;
        list.add(o1.toString(CharsetUtil.UTF_8));//传递到下一个handler
    }
}
