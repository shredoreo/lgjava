package com.shred.juc.demo4;

public class MyQueue2 extends MyQueue{
    private String[] data = new String[10];
    private int putIndex = 0;
    private int getIndex = 0;
    private int size = 0;

    @Override
    public synchronized void put(String element) {
        if (size == data.length) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            put(element);
        } else {
            put0(element);

        }
    }

    private void put0(String element) {
        data[putIndex++] = element;
        size++;
        //唤醒消费者，锁是当前对象，this
        notify();
        if (putIndex == data.length) {
            putIndex = 0;
        }
    }

    @Override
    public synchronized String get() {
        if (size == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return get();
        } else{
            return get0();
        }
    }

    private String get0() {
        String result = data[getIndex++];
        --size;
        //唤醒生产者，锁是当前对象，this
        notify();
        if (getIndex == data.length) getIndex = 0;
        return result;
    }
}
