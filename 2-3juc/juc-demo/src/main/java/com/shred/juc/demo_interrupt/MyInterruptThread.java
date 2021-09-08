package com.shred.juc.demo_interrupt;

public class MyInterruptThread extends Thread{
    @Override
    public void run() {
        while (true){
            boolean interrupted = isInterrupted();
            System.out.println("终端标记；"+ interrupted);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        MyInterruptThread myInterruptThread = new MyInterruptThread();
        myInterruptThread.start();
        Thread.sleep(10);
        myInterruptThread.interrupt();
        Thread.sleep(100);
        System.exit(0);

    }
}
