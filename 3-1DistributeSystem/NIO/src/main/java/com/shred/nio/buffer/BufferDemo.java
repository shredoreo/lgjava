package com.shred.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BufferDemo {
    public static void main(String[] args) {
        ByteBuffer allocate = ByteBuffer.allocate(5);
        for (int i = 0; i < 5; i++) {
            System.out.println(allocate.get());
        }

        ByteBuffer wrap = ByteBuffer.wrap("sherd".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < 5; i++) {
            System.out.println(wrap.get());
        }

    }
}
