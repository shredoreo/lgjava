package com.shred.minicat;

import com.shred.minicat.server.HttpProtocolUtil;
import com.shred.minicat.server.Request;
import com.shred.minicat.server.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BootStrap {
    private int port = 8080;

    /**
     * 初始化展开的操作
     */
    public void start() throws IOException {
        // v1.0 需求：请求http://localhost:8080/ 返回固定的字符串
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("===>>>Minicat start on port:"+ port);
       /* while (true){
            //阻塞，知道获取到socket
            Socket socket = serverSocket.accept();
            OutputStream outputStream = socket.getOutputStream();
            String meg = "hello mini cat";
            String responseText = HttpProtocolUtil.getHttpHeader200(meg.getBytes(StandardCharsets.UTF_8).length) + meg;
            outputStream.write(responseText.getBytes(StandardCharsets.UTF_8));
            socket.close();
        }*/

        /**
         * v2
         */
        while (true){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            response.outputHtml(request.getUrl());

            socket.close();

        }

    }




    public static void main(String[] args) throws IOException {
        new BootStrap().start();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
