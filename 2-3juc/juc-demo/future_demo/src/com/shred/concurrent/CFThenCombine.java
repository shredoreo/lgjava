package com.shred.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

public class CFThenCombine {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future =
                CompletableFuture.supplyAsync(() -> "hello")
                        .thenCombine(
                                CompletableFuture.supplyAsync(() -> "world"),
                                // BiFunction: String,String->String
                                (s, s2) -> s + " " + s2
                        );

        System.out.println(future.get());
    }
}
