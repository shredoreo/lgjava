package com.shred.juc.demo;

public class MyThread extends Thread{

    public synchronized void method(){
        //临界段
    }

    @Override
    public void run() {

        while (true){
            System.out.println(Thread.currentThread().getName()+"运行了。。。");
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
