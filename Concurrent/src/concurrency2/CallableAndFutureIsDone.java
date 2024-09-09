package concurrency2;

import java.util.concurrent.*;

public class CallableAndFutureIsDone {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        System.out.println("Submitting Callable");
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.SECONDS.sleep(2);
                return "Hello from Callable";
            }
        });

        while(!future.isDone()) {
            System.out.println("Task is still not done...");
            TimeUnit.MILLISECONDS.sleep(400);
        }

        System.out.println("Task completed! Retrieving the result");
        String result = future.get();
        System.out.println("Result: " + result);

        executorService.shutdown();
    }
}
