package com.shred.minicat.server.map;

import com.shred.minicat.server.Request;
import com.shred.minicat.server.config.Configuration;
import com.shred.minicat.server.config.MappedContext;
import com.shred.minicat.server.config.MappedHost;
import com.shred.minicat.server.config.Wrapper;
import com.shred.minicat.server.servlet.HttpServlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class Mapper {
    public void map(Request request){
        String hostName = request.getHostName();

        MappingData mappingData = new MappingData();
        request.setMappingData(mappingData);

        internalMapping(request, mappingData);
    }

    private void internalMapping(Request request, MappingData mappingData) {
        // 请求主机
        String hostName = request.getHostName();
        // 请求uri： /app1/a.html
        String url = request.getUrl();

        String[] uri = url.split("/");
        // 请求应用：app1
        String appName = uri[1];

        //urlPattern
        String appUri = "/"+uri[2];


        //查找host
        ArrayList<MappedHost> mappedHosts = Configuration.mappedHosts;
        MappedHost mappedHost1 = mappedHosts.stream()
                .filter(mappedHost -> mappedHost.getName().equals(hostName)).findFirst().get();
        mappingData.mappedHost = mappedHost1;

        //查找webapp
        MappedContext mappedContext1 = mappedHost1.getContextList().stream()
                .filter(mappedContext -> mappedContext.getAppPath().equals(appName)).findFirst().get();
        mappingData.mappedContext = mappedContext1;

        //查找servlet
        HashMap<String, Wrapper> mappedWrapper = mappedContext1.getMappedWrapper();
        Wrapper wrapper = mappedWrapper.get(appUri);
        mappingData.wrapper = wrapper;

    }

}
