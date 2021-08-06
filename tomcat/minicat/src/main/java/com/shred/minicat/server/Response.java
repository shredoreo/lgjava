package com.shred.minicat.server;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 核心方法：输出html
 */
public class Response {

    private OutputStream outputStream;

    public Response() {
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 输出指定字符串
     */
    public void outPut(String content) throws IOException {
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));

    }

    /**
     * 根据url获取静态资源的绝对路径，读取并输出
     * @param url
     */
    public void outputHtml(String url) throws IOException {
        // 获取静态资源的绝对路径
        String absoluteResourcePath = StaticResourceUtil.getAbsolutePath(url);
        // 输入静态资源文件
        File file = new File(absoluteResourcePath);
        if (file.exists() && file.isFile()){
            //存在 输出静态资源
            StaticResourceUtil.outputStaticResource(new FileInputStream(file), outputStream);
        }
        else {
            // 不存在，输出404
            outPut(HttpProtocolUtil.getHttpHeader404());
        }

    }
}
