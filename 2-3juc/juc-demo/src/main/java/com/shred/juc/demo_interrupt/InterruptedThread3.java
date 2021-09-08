package com.shred.juc.demo_interrupt;

public class InterruptedThread3 extends Thread{

    @Override
    public void run() {
        System.out.println("线程开始执行");
        int i =0;
        while (true){
            boolean interrupted = isInterrupted();
            System.out.println("isInterrupted()中断标记："+ interrupted);

            ++i;
            if (i>200){
                //检查并重置 中断标志
                boolean interrupted1 = Thread.interrupted();
                System.out.println("Thread.interrupted()重置中断状态："+interrupted1);
                interrupted1 = Thread.interrupted();
                System.out.println("Thread.interrupted()重置中断状态："+interrupted1);

                 interrupted = isInterrupted();
                System.out.println("isInterrupted()中断标记："+ interrupted);
                break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        InterruptedThread3 interruptedThread3 = new InterruptedThread3();
        interruptedThread3.start();
        Thread.sleep(10);
        System.out.println("main 即将发起中断");
        interruptedThread3.interrupt();
        System.out.println("main 结束发起中断");

        Thread.sleep(7);

        System.out.println("main检查中断状态-1 : "+ interruptedThread3.isInterrupted());
        System.out.println("main检查中断状态-2 : "+ interruptedThread3.isInterrupted());

    }
}
