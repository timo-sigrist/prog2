package concurrency2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolCPUExample {
    public static void main(String[] args) {
        System.out.println("Inside Thread : " + Thread.currentThread().getName());
        int cores = Runtime.getRuntime().availableProcessors();

        System.out.println("Creating Executor Service with thread pool size = number of cores: " + cores);
        ExecutorService executorService = Executors.newFixedThreadPool(cores);

        Runnable task1 = new PoolTask(1, 2);
        Runnable task2 = new PoolTask(2, 4);
        Runnable task3 = new PoolTask(3, 3);

        System.out.println("Submitting the tasks for execution...");
        executorService.submit(task1);
        executorService.submit(task2);
        executorService.submit(task3);

        System.out.println("Shutting down the Executor Service");
        executorService.shutdown();
    }

    private static class PoolTask implements Runnable {
        private final int id;
        private final int timeout;

        private PoolTask(int id, int timeout) {
            this.id = id;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            System.out.println("Executing Task " + id + " inside Thread : " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(timeout);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }
            System.out.println("Ending Task " + id);
        }
    }
}
