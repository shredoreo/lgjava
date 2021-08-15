package com.shred.nio.buffer;

import java.nio.ByteBuffer;

public class PutBufferDeom {
    public static void main(String[] args) {
        ByteBuffer allocate = ByteBuffer.allocate(10);
        //获取当前索引所在位置
        System.out.println(allocate.position());

        //最多操作的索引
        System.out.println(allocate.limit());

        //缓冲区总长度
        System.out.println(allocate.capacity());

        //  还有几个可操作的索引
        System.out.println(allocate.remaining());


    }
}
