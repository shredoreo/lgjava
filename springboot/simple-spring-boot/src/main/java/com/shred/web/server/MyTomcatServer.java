package com.shred.web.server;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;

public class MyTomcatServer {
    /**
     * 完成
     * tomcat的创建及启动
     */
    public static void runTom(){

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        File tempDir = null;
        try {
            tempDir = createTempDir("tomcat");
            tomcat.addWebapp("/", tempDir.getAbsolutePath());
            //启动tomcat
            tomcat.start();

            System.out.println("server started at port 8080");

            //阻塞当前线程
            tomcat.getServer().await();

        } catch (LifecycleException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 创建tomcat运行的临时目录
     * @param prefix
     * @return
     * @throws Exception
     */
    static File createTempDir(String prefix) throws Exception {
        try {
            File tempDir = File.createTempFile(prefix + ".", "." + 8080);
            tempDir.delete();
            tempDir.mkdir();
            tempDir.deleteOnExit();
            return tempDir;
        } catch (IOException ex) {
            throw new Exception(
                    "Unable to create tempDir. java.io.tmpdir is set to " + System.getProperty("java.io.tmpdir"), ex);
        }
    }

}
