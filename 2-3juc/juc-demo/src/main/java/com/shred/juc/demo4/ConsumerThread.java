package com.shred.juc.demo4;

import java.util.Random;

public class ConsumerThread extends Thread{

    private final MyQueue myQueue;
    private Random random = new Random();

    public ConsumerThread(MyQueue myQueue) {
        this.myQueue = myQueue;
    }

    @Override
    public void run() {
        while (true){
            String s = myQueue.get();
            System.out.println("消费到到数据L："+s);
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
