package com.shred.c;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MyThread extends Thread{
    private final CountDownLatch latch;
    private final Random random = new Random();

    public MyThread(String name,CountDownLatch latch) {
        super(name);
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(random.nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"- 执行完毕");

        latch.countDown();
    }
}
