package com.shred.minicat;

import com.shred.minicat.server.HttpProtocolUtil;
import com.shred.minicat.server.Request;
import com.shred.minicat.server.Response;
import com.shred.minicat.server.servlet.HttpServlet;
import com.shred.minicat.server.thread.RequestProcessor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class BootStrap {
    private int port = 8080;
    private HashMap<String, HttpServlet> servletMap = new HashMap<>();

    /**
     * 初始化展开的操作
     */
    public void start() throws Exception {
//        loadConfig();
//        loadServlet();

        //创建线程池
        ThreadPoolExecutor threadPoolExecutor = accuireThreadPoolExecutor();


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
         * v2 请求静态资源
         */
       /* while (true){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            response.outputHtml(request.getUrl());

            socket.close();
        }*/

        /**
         * v3 请求动态资源：servlet
         */
        /*while (true){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            String url = request.getUrl();

            // 处理请求
            HttpServlet httpServlet = servletMap.get(url);
            if (httpServlet == null){
                response.outputHtml(url);
            } else {
                httpServlet.service(request,response);
            }

            socket.close();
        }*/

        /**
         * v3.1 多线程改造
         */
       /* while (true){
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket, servletMap);
            requestProcessor.start();
        }*/

        /**
         * v3.2 多线程改造 使用 线程池
         */
        while (true){
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket, servletMap);
            threadPoolExecutor.execute(requestProcessor);
        }
    }



    private ThreadPoolExecutor accuireThreadPoolExecutor() {
        int corePoolSize = 10;
        int maximumPoolSize = 50;
        long keepAliveTime = 100L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory =  Executors.defaultThreadFactory();
        RejectedExecutionHandler defaultHandler = new ThreadPoolExecutor.AbortPolicy();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory, defaultHandler
        );
        return threadPoolExecutor;
    }






    public static void main(String[] args) throws Exception {
        new BootStrap().start();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
