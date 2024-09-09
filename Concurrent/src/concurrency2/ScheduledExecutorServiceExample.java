package concurrency2;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceExample {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        System.out.println("Submitting Task 1 to be executed after 4s.");
        scheduledExecutorService.schedule(new ScheduledTask(1, System.currentTimeMillis()), 4, TimeUnit.SECONDS);

        System.out.println("Submitting Task 2 to be executed after 4s and repeated every 3s.");
        scheduledExecutorService.scheduleAtFixedRate(new ScheduledTask(2, System.currentTimeMillis()), 4, 3, TimeUnit.SECONDS);

        System.out.println("Submitting Task 3 to be executed after 2s and rerun after delay of 3s.");
        scheduledExecutorService.scheduleWithFixedDelay(new ScheduledTask(3, System.currentTimeMillis()), 2, 3, TimeUnit.SECONDS);

        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());
        }
        System.out.println("Shutting down the executor");
        scheduledExecutorService.shutdown();
    }

    private static class ScheduledTask implements Runnable {
        private final int id;
        private final long starttime;

        private ScheduledTask(int id, long starttime) {
            this.id = id;
            this.starttime = starttime;
        }

        @Override
        public void run() {
            System.out.println("Executing Task " + id +
                " at " + (System.currentTimeMillis()-starttime) + " ms after start" +
                " in Thread : " + Thread.currentThread().getName());
        }
    }

}
