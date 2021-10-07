package com.shred.c;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class MyThread extends Thread{
    private final Semaphore semaphore;
    private final Random random = new Random();

    public MyThread(String name,Semaphore semaphore) {
        super(name);
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName()+"抢到了座位");
            Thread.sleep(random.nextInt(1000));
            System.out.println(Thread.currentThread().getName()+"洗完，让出座位");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //释放信标，腾出作为
        semaphore.release();

    }
}
