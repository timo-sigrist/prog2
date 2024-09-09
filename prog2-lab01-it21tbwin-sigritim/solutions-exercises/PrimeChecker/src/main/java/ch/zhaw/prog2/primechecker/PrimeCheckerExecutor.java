package ch.zhaw.prog2.primechecker;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.*;

public class PrimeCheckerExecutor {

    private static final long LOWER_LIMIT = 10000L;
    private static final long UPPER_LIMIT = 1000000000L;
    private static final int NUM_PRIME = 500;
    private static StringWriter statsWriter = new StringWriter();
    private static PrintWriter stats = new PrintWriter(statsWriter);

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
        // determine number of cores
        int numCores = Runtime.getRuntime().availableProcessors();
        // create ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(numCores);
        // ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < numPrimes; i++) {
            // execute the runnable using the executor service
            executor.execute(new PrimeTask(nextRandom()));
            // collect executor statistics every 100 tasks
            if (i % 100 == 0) stats.println("Executor Info after " + i + " tasks: " + executor);
        }
        // stop ExecutorService
        executor.shutdown();
        // wait for termination with timeout of 1 minute
        executor.awaitTermination(1, TimeUnit.MINUTES);
        stats.println("Executor Info after termination: " + executor);
        // print executor statistics
        System.out.print(statsWriter.toString());
    }

    private static long nextRandom() {
        return LOWER_LIMIT + (long)(Math.random() * (UPPER_LIMIT - LOWER_LIMIT));
    }
}
