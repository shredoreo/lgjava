package com.shred.lock;

import java.util.concurrent.locks.ReentrantLock;

public class Test1 {
//    static ReentrantLock lock = new ReentrantLock();
    static MyLock lock = new MyLock();

    public static void main(String[] args) {

        new Thread(() -> {
            lock.lock();
            drawMoney();
            lock.unlock();
        }, "线程1").start();


        new Thread(() -> {
            lock.lock();
            drawMoney();
            lock.unlock();
        }, "线程2").start();


    }

    private static void drawMoney(){
        System.out.println(Thread.currentThread().getName() + " 正在取钱");
        try {

            Thread.sleep(10
            );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " 取钱好了");

    }

}
