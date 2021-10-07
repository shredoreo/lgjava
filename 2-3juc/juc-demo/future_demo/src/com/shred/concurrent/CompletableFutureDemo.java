package com.shred.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture future= new CompletableFuture<String>();



        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.complete("sdsdfsf");
        }).start();
        System.out.println("任务");
        Object o = future.get();
        System.out.println(o);

    }
}
