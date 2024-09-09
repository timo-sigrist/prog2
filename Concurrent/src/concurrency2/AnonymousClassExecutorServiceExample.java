package concurrency2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnonymousClassExecutorServiceExample {
    public static void main(String[] args) {
        System.out.println("Inside thread : " + Thread.currentThread().getName());

        System.out.println("Creating Executor Service...");
        ExecutorService executorService = Executors.newWorkStealingPool();//newSingleThreadExecutor();

        System.out.println("Submit the task specified by the runnable to the executor service.");
        executorService.execute(new SimpleTask());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Inside anonymous task 1");
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Inside anonymous task 2");
            }
        });
        executorService.execute(() -> System.out.println("Inside anonymous task 3"));

        System.out.println("Shutting down the executor");
        executorService.shutdown();

        System.out.println("Main ends");
    }
}
