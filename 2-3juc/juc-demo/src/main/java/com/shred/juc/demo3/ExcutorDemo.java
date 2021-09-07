package com.shred.juc.demo3;

import java.util.concurrent.*;

public class ExcutorDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5, 5, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10)){
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                // 如果call方法执行中出现错误，可在此处做处理
                super.afterExecute(r, t);
                System.out.println("任务执行完毕"+t);
            }
        };

        MyCallable myCallable = new MyCallable();
        Future<String> future = executor.submit(myCallable);

        String s = future.get();
        System.out.println(s);

        //关闭线程池
        executor.shutdown();
    }
}
