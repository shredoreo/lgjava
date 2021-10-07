package com.shred.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class CFComposeDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ssss";
        }).thenCompose(new Function<String, CompletableFuture<Integer>>() {
            @Override
            public CompletableFuture<Integer> apply(String s) {
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return s.length();
                });
            }
        });

        System.out.println(future.get());
    }


   /* public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<CompletableFuture<Integer>> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ssss";
        }).thenApply(new Function<String, CompletableFuture<Integer>>() {
            @Override
            public CompletableFuture<Integer> apply(String s) {
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return s.length();
                });
            }
        });

        System.out.println(future.get().get());
    }*/
}
