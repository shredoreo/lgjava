package com.shred.juc.demo4;

public class MyQueue {
    private String[] data = new String[10];
    private int putIndex = 0;
    private int getIndex = 0;
    private int size = 0;

    public synchronized void put(String element) {
        if (size == data.length) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        data[putIndex++] = element;
        size++;
        //唤醒消费者，锁是当前对象，this
        notify();
        if (putIndex == data.length) {
            putIndex = 0;
        }
    }

    public synchronized String get() {
        if (size == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String result = data[getIndex++];
        --size;
        //唤醒生产者，锁是当前对象，this
        notify();
        if (getIndex == data.length) getIndex = 0;
        return result;
    }

}
