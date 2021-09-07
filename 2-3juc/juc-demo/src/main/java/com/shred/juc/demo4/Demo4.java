package com.shred.juc.demo4;

public class Demo4 {
    public static void main(String[] args) {
        MyQueue myQueue = new MyQueue();

        new ConsumerThread(myQueue).start();
        new ProducerThread(myQueue).start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
