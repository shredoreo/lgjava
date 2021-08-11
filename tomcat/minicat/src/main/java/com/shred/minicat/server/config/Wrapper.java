package com.shred.minicat.server.config;

import com.shred.minicat.server.servlet.HttpServlet;

public class Wrapper {
    private HttpServlet httpServlet;

    public HttpServlet getHttpServlet() {
        return httpServlet;
    }

    public void setHttpServlet(HttpServlet httpServlet) {
        this.httpServlet = httpServlet;
    }
}
