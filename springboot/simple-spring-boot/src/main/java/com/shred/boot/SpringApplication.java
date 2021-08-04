package com.shred.boot;

import com.shred.web.server.MyTomcatServer;

public class SpringApplication {

    public static void main(String[] args) throws Exception {
        new SpringApplication().run();
    }

    public  void run() throws Exception {

        onRefresh();

    }

    private void onRefresh() throws Exception {
        //启动tomcat
        MyTomcatServer.runTom();
    }

}
