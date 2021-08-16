package com.shred.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NIOServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //服务端绑定端口
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 9991));
        //取消阻塞
        serverSocketChannel.configureBlocking(false);
        System.out.println("客户端启动成功");
        while (true){
            SocketChannel accept = serverSocketChannel.accept();
            if (accept== null){
                System.out.println("没有客户端连接。。处理其他事情");
                Thread.sleep(2000);
                continue;
            }
            ByteBuffer bf = ByteBuffer.allocate(1024);

            //返回值:
//正数: 表示本次读到的有效字节个数.
//0 : 表示本次没有读到有效字节.
//-1 : 表示读到了末尾
            int read = accept.read(bf);
            System.out.println("客户端消息："+ new String(bf.array(), 0, read, StandardCharsets.UTF_8));

            // 给客户端会写数据
            accept.write(ByteBuffer.wrap("没钱".getBytes(StandardCharsets.UTF_8)));

            //关闭资源
            accept.close();
        }


    }
}
