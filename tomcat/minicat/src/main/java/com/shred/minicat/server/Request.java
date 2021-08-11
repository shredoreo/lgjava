package com.shred.minicat.server;

import com.shred.minicat.server.map.MappingData;

import java.io.IOException;
import java.io.InputStream;

/**
 * 根据InputStream封装
 */
public class Request {

    /**
     * 请求方式get post
     */
    private String method;

    private String url;// 如 /, /index.html

    private InputStream inputStream;//输入流，其他属性通过输入流获取
    /**
     * 端口
     */
    private String port;
    /**
     * 主机名
     */
    private String hostName;

    private MappingData mappingData;

    public Request() {
    }

    public MappingData getMappingData() {
        return mappingData;
    }

    public void setMappingData(MappingData mappingData) {
        this.mappingData = mappingData;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;

        //从输入流获取请求信息
        int count = 0;
        while (count == 0){
            //请求不存在时，不断读取
            count = inputStream.available();
        }
        byte[] bytes = new byte[count];
        inputStream.read(bytes);
        String requestText = new String(bytes);
        System.out.println("====>>请求信息: "+ requestText);
        String[] splitLine = requestText.split("\\n");
        //获取第一行请求头信息
        resolveHeader(splitLine[0]);
        //获取第二行 请求主机
        resolveHost(splitLine[1]);

    }

    private void resolveHost(String hostStr) {
        //Host: localhost:8080
        String[] split = hostStr.split(" ");
        //localhost:8080
        String[] sp = split[1].split(":");
        //localhost
        this.hostName = sp[0];
        //8080
        String port = sp[1];
        this.port = port;


    }

    private void resolveHeader(String line11) {
        String line1 = line11;
        String[] split = line1.split(" ");
        this.method = split[0];
        this.url = split[1];
        System.out.println("--->method:"+ method);
        System.out.println("--->url:"+ url);
    }
}
