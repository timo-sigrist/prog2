package concurrency2;

import java.util.concurrent.*;

public class CallableAndFuture {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                // Perform some computation
                System.out.println("Entered Callable, sleeps...");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("...done sleeping in Callable");

                return "Hello from Callable";
            }
        };

        System.out.println("Submitting Callable");
        Future<String> future = executorService.submit(callable);

        // This line executes immediately
        System.out.println("Do something else while callable is executed");
        TimeUnit.MILLISECONDS.sleep(200);

        System.out.println("Retrieve the result of the future");
        // Future.get() blocks until the result is available
        String result = future.get();
        System.out.println("Result: " + result);

        System.out.println("Shutting down the executor");
        executorService.shutdown();
    }

}
