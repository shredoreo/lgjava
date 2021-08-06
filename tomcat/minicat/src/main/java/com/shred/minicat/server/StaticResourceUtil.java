package com.shred.minicat.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StaticResourceUtil {
    /**
     * 获取静态资源文件的绝对路径
     * @param path
     * @return
     */
    public static String getAbsolutePath(String path){
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        //转换地址
        return absolutePath.replaceAll("\\\\","/") +path;
    }


    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {

        int count = 0;
        while (count == 0){
            count = inputStream.available();
        }
        int resourceSize = count;
        // 输入http请求头，再输出内容
        outputStream.write(HttpProtocolUtil.getHttpHeader200(resourceSize).getBytes(StandardCharsets.UTF_8));

        // 已读
        long written = 0;
        int byteSize = 1024;
        byte[] bytes = new byte[byteSize];

        while (written < resourceSize){
            //剩余不足1024 计算剩余
            if (byteSize < resourceSize - written){
                byteSize = (int) (resourceSize - written);
                bytes = new byte[byteSize];
            }

            inputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();
            written+=byteSize;
        }



    }
}
