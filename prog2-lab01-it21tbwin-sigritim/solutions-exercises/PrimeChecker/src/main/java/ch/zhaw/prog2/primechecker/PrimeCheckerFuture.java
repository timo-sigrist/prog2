package ch.zhaw.prog2.primechecker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PrimeCheckerFuture {

    private static final long LOWER_LIMIT = 10000L;
    private static final long UPPER_LIMIT = 1000000000L;
    private static final int NUM_PRIME = 500;

    public static void main(String[] args) {
        long starttime = System.currentTimeMillis();
        long duration;
        try {
            checkPrimes(NUM_PRIME);
        } catch (InterruptedException e) {
            System.out.println("Interrupted - " + e.getMessage());
        } finally {
            duration = System.currentTimeMillis() - starttime;
        }
        System.out.println("Finished in " + duration + " ms");
    }

    private static void checkPrimes(int numPrimes) throws InterruptedException {
        // create ExecutorService
        int numCores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numCores);
        // submit tasks to ExecutorService and collect the returned Futures in a List
        List<Future<PrimeTaskCallable.Result>> tasks = new ArrayList<>();
        for (int i = 0; i < numPrimes; i++) {
            // execute runnable / submit callable
            tasks.add(executor.submit(new PrimeTaskCallable(nextRandom())));
        }
        // Loop through List, wait for completion and print results
        for (Future<PrimeTaskCallable.Result> task : tasks) {
            try {
                PrimeTaskCallable.Result result = task.get();
                System.out.println("Number: " + result.candidate + "  -> " +
                    (result.isPrime ? "PRIME" : "Factor: " + result.factor));
            } catch (ExecutionException e) {
                System.out.println("Execution failed : " + e.getMessage());
            }
        }
        // stop ExecutorService
        executor.shutdown();
        // await termination with timeout 1 minute
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    private static long nextRandom() {
        return LOWER_LIMIT + (long)(Math.random() * (UPPER_LIMIT - LOWER_LIMIT));
    }
}
