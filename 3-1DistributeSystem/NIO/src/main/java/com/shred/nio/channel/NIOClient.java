package com.shred.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NIOClient {
    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        //客户端连接
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));

        System.out.println("客户端器启动成功");
        socketChannel.write(ByteBuffer.wrap("老板。还钱".getBytes(StandardCharsets.UTF_8)));
        ByteBuffer bf = ByteBuffer.allocate(1024);
        int read = socketChannel.read(bf);
        System.out.println("服务端消息：" + new String(bf.array(), 0, read));
        socketChannel.close();
    }
}
