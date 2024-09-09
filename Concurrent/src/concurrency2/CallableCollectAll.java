package concurrency2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class CallableCollectAll {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Callable<String> task1 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                return "Result of Task1";
            }
        };

        Callable<String> task2 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000);
                return "Result of Task2";
            }
        };

        Callable<String> task3 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                return "Result of Task3";
            }
        };

        List<Callable<String>> taskList = Arrays.asList(task1, task2, task3);
        List<Future<String>> futureList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (Callable<String> task : taskList) {
            futureList.add(executorService.submit(task));
        }

        for(Future<String> future: futureList) {
            // Results are printed as soon the result of the next task is available.
            System.out.println(future.get() + " after " + (System.currentTimeMillis()-startTime) + "ms");
        }

        executorService.shutdown();
    }
}
