package com.shred.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerDemo {

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("服务已启动");

        while (true){
            Socket accept = serverSocket.accept();

            System.out.println("客户端连接");
            executorService.execute(()->{
                handle(accept);
            });
        }

    }

    private static void handle(Socket socket) {

        System.out.printf("线程%s  名称%s%n",Thread.currentThread().getId(), Thread.currentThread().getName());

        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int read = inputStream.read(buffer);
            System.out.println("客户端："+ new String(buffer, 0, read));

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("没钱".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
