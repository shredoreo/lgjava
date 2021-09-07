package com.shred.juc.demo4;

import java.util.Random;

public class ProducerThread extends Thread{
    private final MyQueue myQueue;
    private int index =0;
    private Random random = new Random();

    public ProducerThread(MyQueue myQueue) {
        this.myQueue = myQueue;
    }

    @Override
    public void run() {
        while (true){
            System.out.println("生产者生成数据："+ index);
            myQueue.put("sc "+index);
            index++;
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
