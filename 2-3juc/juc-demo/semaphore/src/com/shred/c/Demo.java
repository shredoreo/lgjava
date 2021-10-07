package com.shred.c;

import java.util.concurrent.Semaphore;

public class Demo {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);

        for (int i = 0; i < 5; i++) {
         new MyThread("学生"+(i+1), semaphore).start();
        }
    }
}
