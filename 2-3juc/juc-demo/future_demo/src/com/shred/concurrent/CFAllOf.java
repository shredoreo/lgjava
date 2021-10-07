package com.shred.concurrent;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CFAllOf {
    private static Random random = new Random();
    private static volatile int result = 0;
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture[] futures = new CompletableFuture[10];
        for (int i = 0; i < 10; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result++;
            });
            futures[i] = future;

        }

     /*   for (Future future : futures) {
            future.get();
            System.out.println(result);
        }
*/
        CompletableFuture.allOf(futures).get();
        System.out.println(result);
    }
}
