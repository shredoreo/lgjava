package com.shred.juc.demo_destory;

public class MyDestoryThread extends Thread{

    //线程关闭的标志位
    private boolean running =true;

    @Override
    public void run() {
         while (running){
             System.out.println(Thread.currentThread().getName()+ " 线程 running");
             try {
                 Thread.sleep(1000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
    }

    public void stopRunning(){
        System.out.println("清除running标志");
        this.running = false;
    }

    public static void main(String[] args) throws InterruptedException {
        MyDestoryThread myDestoryThread = new MyDestoryThread();
        myDestoryThread.start();

        Thread.sleep(2222);

        myDestoryThread.stopRunning();
        myDestoryThread.join();
    }
}
