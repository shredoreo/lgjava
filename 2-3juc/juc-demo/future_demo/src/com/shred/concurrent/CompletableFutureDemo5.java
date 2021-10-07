package com.shred.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class CompletableFutureDemo5 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String >() {
            @Override
            public String get() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "hello";
            }
        });


        CompletableFuture<Void> thenAccept = future.thenAccept(res -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("then accepted..." + res);
        });

        String s = future.get();
        System.out.println(s);
        Void unused = thenAccept.get();
        System.out.println("执行结束："+unused);

    }
}
