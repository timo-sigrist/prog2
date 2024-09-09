package concurrency2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceExample {

    public static void main(String[] args) {
        System.out.println("Inside thread : " + Thread.currentThread().getName());

        System.out.println("Creating Executor Service...");
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        System.out.println("Creating a Task...");
        Runnable runnable = new SimpleTask();

        System.out.println("Submit the task specified by the runnable to the executor service.");
        executorService.execute(runnable);

        System.out.println("Main ends");
    }
}
