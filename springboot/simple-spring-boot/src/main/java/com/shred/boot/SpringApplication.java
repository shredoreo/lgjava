package com.shred.boot;

import com.shred.servlet.DemoServlet;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

public class SpringApplication {

    public static void main(String[] args) throws Exception {
        new SpringApplication().run();
    }

    public  void run() throws Exception {

        onRefresh();

    }

    private void onRefresh() throws Exception {
        // 创建web服务器
//        createWebServer();

        runTom();
//        TomcatStart.run();
    }


    public void runTom(){
        /**
         * 完成
         * tomcat的创建及启动
         */
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        File tempDir = null;
        try {
            tempDir = createTempDir("tomcat");
            tomcat.addWebapp("/", tempDir.getAbsolutePath());
            tomcat.start();
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
    File createTempDir(String prefix) throws Exception {
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

    private void createWebServer() throws Exception {
        Tomcat tomcat = new Tomcat();
//        tomcat.setBaseDir("/tomcat");


        Connector conn=new Connector("HTTP/1.1");
        conn.setPort(8080);

        tomcat.getService().addConnector(conn);

        tomcat.start();
        //4.阻塞当前线程
        tomcat.getServer().await();
    }

}
