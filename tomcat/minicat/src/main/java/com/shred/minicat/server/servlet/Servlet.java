package com.shred.minicat.server.servlet;

import com.shred.minicat.server.Request;
import com.shred.minicat.server.Response;

public interface Servlet {

    void init()throws Exception;
    void destory() throws Exception;
    void service(Request request, Response response) throws Exception;


}
