package com.shred.juc.demo_interrupt;

public class MyInterruptThread2 extends Thread{

    @Override
    public void run() {
        try {
            //sleep被中断时会抛出异常
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyInterruptThread2 myInterruptThread2 = new MyInterruptThread2();
        myInterruptThread2.start();
        Thread.sleep(10);

        //中断
        myInterruptThread2.interrupt();
        Thread.sleep(100);
        System.exit(0);

    }
}
