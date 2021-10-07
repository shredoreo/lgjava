package com.shred.c;

import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new MyThread("线程"+(i+1), countDownLatch).start();

        }
        countDownLatch.await();
        System.out.println("main 执行结束");
    }
}
