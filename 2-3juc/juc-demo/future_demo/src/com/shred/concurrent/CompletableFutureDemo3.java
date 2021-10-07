package com.shred.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class CompletableFutureDemo3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> voidCompletableFuture = CompletableFuture.supplyAsync(new Supplier<String >() {
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

        System.out.println(voidCompletableFuture.get());

    }
}
