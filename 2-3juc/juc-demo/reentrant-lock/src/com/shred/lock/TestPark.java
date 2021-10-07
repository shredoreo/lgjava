package com.shred.lock;

import java.util.concurrent.locks.LockSupport;

public class TestPark {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "正在执行");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "继续执行");
        }, "线程1");

        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "开始执行");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "解放");
            LockSupport.unpark(thread);
        }, "线程2");
        thread.start();
        thread1.start();

    }

}
