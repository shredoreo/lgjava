package com.shred.juc.demo_deamon;

public class MyDemonThread extends Thread {
    @Override
    public void run() {
        while (true) {
            System.out.println("DEMON: "+ Thread.currentThread().getName());

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        MyDemonThread myDemonThread = new MyDemonThread();
        myDemonThread.setDaemon(true);
        myDemonThread.start();
//        new MyNonDemonThread().start();
    }
}
