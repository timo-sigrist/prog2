package concurrency2;

import java.util.concurrent.*;

public class CallableAndFutureCancel {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        long startTime = System.currentTimeMillis();
        System.out.println("Submitting Callable");
        Future<String> future = executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(2);
            return "Hello from Callable";
        });

        while(!future.isDone()) {
            System.out.println("Task is still not done...");
            TimeUnit.MILLISECONDS.sleep(400);
            double elapsedTimeInMillis = System.currentTimeMillis() - startTime;
            if(elapsedTimeInMillis > 1000) {
                future.cancel(true);
            }
        }
        if(!future.isCancelled()) {
            System.out.println("Task completed! Retrieving the result");
            String result = future.get();
            System.out.println("Result: " + result);
        } else {
            System.out.println("Task was cancelled");
        }
        executorService.shutdown();
    }
}
