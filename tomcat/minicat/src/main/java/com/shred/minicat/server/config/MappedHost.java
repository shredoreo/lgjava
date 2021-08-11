package com.shred.minicat.server.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MappedHost {
    String name;
    String appBase;
    List<MappedContext> contextList = new ArrayList<>();

    public String getAppBase() {
        return appBase;
    }

    public void setAppBase(String appBase) {
        this.appBase = appBase;
    }

    public List<MappedContext> getContextList() {
        return contextList;
    }

    public void setContextList(List<MappedContext> contextList) {
        this.contextList = contextList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
