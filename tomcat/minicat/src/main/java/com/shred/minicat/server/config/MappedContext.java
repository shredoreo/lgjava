package com.shred.minicat.server.config;

import com.shred.minicat.server.servlet.HttpServlet;

import java.util.HashMap;

public class MappedContext {
    private String appPath;

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public HashMap<String, HttpServlet> getServletMap() {
        return servletMap;
    }

    public void setServletMap(HashMap<String, HttpServlet> servletMap) {
        this.servletMap = servletMap;
    }

    private HashMap<String, HttpServlet> servletMap = new HashMap<>();
}
