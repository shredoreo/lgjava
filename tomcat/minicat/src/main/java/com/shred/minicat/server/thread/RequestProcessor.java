package com.shred.minicat.server.thread;

import com.shred.minicat.server.Request;
import com.shred.minicat.server.Response;
import com.shred.minicat.server.config.Wrapper;
import com.shred.minicat.server.map.Mapper;
import com.shred.minicat.server.map.MappingData;
import com.shred.minicat.server.servlet.HttpServlet;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class RequestProcessor extends Thread{
    private Socket socket;
    private Map<String, HttpServlet> servletMap;

    public RequestProcessor(Socket socket) {
        this.socket = socket;
    }

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

            //使用mapper处理request中的mappingData
            new Mapper().map(request);
            MappingData mappingData = request.getMappingData();

            // 处理请求
            Wrapper wrapper = mappingData.getWrapper();
            if (wrapper == null){
                response.outputHtml(url);
            }
            HttpServlet httpServlet = wrapper.getHttpServlet();
            // 使用mapper处理，获取servlet


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
