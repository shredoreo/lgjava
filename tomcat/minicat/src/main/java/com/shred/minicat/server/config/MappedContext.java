package com.shred.minicat.server.config;

import com.shred.minicat.server.servlet.HttpServlet;

import java.util.HashMap;

public class MappedContext {
    private String appPath;
    private HashMap<String, Wrapper> mappedWrapper = new HashMap<>();

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public HashMap<String, Wrapper> getMappedWrapper() {
        return mappedWrapper;
    }

    public void setMappedWrapper(HashMap<String, Wrapper> mappedWrapper) {
        this.mappedWrapper = mappedWrapper;
    }
}
