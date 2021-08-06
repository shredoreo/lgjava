package com.shred.minicat.server.thread;

import com.shred.minicat.server.Request;
import com.shred.minicat.server.Response;
import com.shred.minicat.server.servlet.HttpServlet;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class RequestProcessor extends Thread{
    private Socket socket;
    private Map<String, HttpServlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {

        try {
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
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
