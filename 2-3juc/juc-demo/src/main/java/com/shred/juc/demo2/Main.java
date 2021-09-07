package com.shred.juc.demo2;

import com.shred.juc.demo2.MyThread;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        MyThread myThread = new MyThread();
        myThread.start();
        myThread.join();
        System.out.println("main 执行完成");
    }
}
