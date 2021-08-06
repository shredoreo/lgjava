package com.shred.minicat.server.servlet;


import com.shred.minicat.server.Request;
import com.shred.minicat.server.Response;

public  abstract class HttpServlet implements Servlet{

    public abstract void doGet(Request request, Response response );
    public abstract void doPost(Request request, Response response );

    @Override
    public void service(Request request, Response response) throws Exception {
        Thread.sleep(10000);
        if ("GET".equalsIgnoreCase(request.getMethod())){
            doGet(request, response);
        } else if ("POST".equalsIgnoreCase(request.getMethod())){
            doPost(request, response);
        }
    }
}
