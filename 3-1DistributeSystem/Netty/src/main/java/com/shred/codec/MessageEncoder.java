package com.shred.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class MessageEncoder extends MessageToMessageEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
        String msg = (String) o;
        System.out.println("进行消息编码....");
        list.add(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));//传递到下一个handler
    }
}
