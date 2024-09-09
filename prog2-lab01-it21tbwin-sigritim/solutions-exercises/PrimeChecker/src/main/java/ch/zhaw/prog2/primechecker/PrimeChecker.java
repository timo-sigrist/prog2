package ch.zhaw.prog2.primechecker;

import java.util.ArrayList;
import java.util.List;

public class PrimeChecker {

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
        List<Thread> tasks = new ArrayList<>();
        for (int i = 0; i < numPrimes; i++) {
            //new PrimeTask(nextRandom()).run(); // runs sequential in current thread
            Thread task = new Thread(new PrimeTask(nextRandom()));
            tasks.add(task);
            task.start();
        }
        for (Thread task: tasks) {
            task.join();
        }
    }

    private static long nextRandom() {
        return LOWER_LIMIT + (long)(Math.random() * (UPPER_LIMIT - LOWER_LIMIT));
    }
}
