package com.shred.juc.demo4;

public class Demo44 {
    public static void main(String[] args) {

        MyQueue myQueue = new MyQueue2();
        for (int i = 0; i < 5; i++) {

            new ConsumerThread(myQueue).start();
            new ProducerThread(myQueue).start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
