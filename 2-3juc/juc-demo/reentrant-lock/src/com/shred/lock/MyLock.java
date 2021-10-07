package com.shred.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class MyLock {
    private static Unsafe unsafe = getUnsafe();

    private int state = 0;

    //state 属性的偏移量
    private static long stateOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(MyLock.class.getDeclaredField("state"));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void lock() {
        //不断由0改成1，成功了就跳出while，说明获取到锁了
        while (!unsafe.compareAndSwapInt(this, stateOffset, 0, 1)) {
            System.out.println(Thread.currentThread().getName() + "正在加锁");
        }
        System.out.println(Thread.currentThread().getName() + "加到锁了");

    }

    public void unlock() {

        state =0;

    }

    private static Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
