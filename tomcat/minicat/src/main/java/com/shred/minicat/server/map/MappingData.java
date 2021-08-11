package com.shred.minicat.server.map;

import com.shred.minicat.server.config.MappedContext;
import com.shred.minicat.server.config.MappedHost;
import com.shred.minicat.server.config.Wrapper;

public class MappingData {
    MappedHost mappedHost;
    MappedContext mappedContext;
    Wrapper wrapper;

    public MappedHost getMappedHost() {
        return mappedHost;
    }

    public void setMappedHost(MappedHost mappedHost) {
        this.mappedHost = mappedHost;
    }

    public MappedContext getMappedContext() {
        return mappedContext;
    }

    public void setMappedContext(MappedContext mappedContext) {
        this.mappedContext = mappedContext;
    }

    public Wrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(Wrapper wrapper) {
        this.wrapper = wrapper;
    }
}
