package com.shred.minicat.server.thread;

import com.shred.minicat.BootStrap;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PortProcessor extends Thread{

    private int port;

    public PortProcessor(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("===>>>Minicat start on port:"+ port);
            while (true){
                Socket accept = serverSocket.accept();

                RequestProcessor requestProcessor = new RequestProcessor(accept);
                BootStrap.accuireThreadPoolExecutor().execute(requestProcessor);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
