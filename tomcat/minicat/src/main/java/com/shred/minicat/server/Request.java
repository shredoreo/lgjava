package com.shred.minicat.server;

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

    public Request() {
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
        //获取第一行请求头信息
        String line1 = requestText.split("\\n")[0];
        String[] split = line1.split(" ");
        this.method = split[0];
        this.url = split[1];
        System.out.println("--->method:"+ method);
        System.out.println("--->url:"+ url);

    }
}
