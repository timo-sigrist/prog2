package concurrency2;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class CallableInvokeAny {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Callable<String> task1 = () -> {
            Thread.sleep(2000);
            //throw new NullPointerException("Error in Task1");
            return "Result of Task1";
        };

        Callable<String> task2 = () -> {
            Thread.sleep(1000);
            //throw new NullPointerException("Error in Task2");
            return "Result of Task2";
        };

        Callable<String> task3 = () -> {
            Thread.sleep(5000);
            //throw new NullPointerException("Error in Task3");
            return "Result of Task3";
        };

        List<Callable<String>> taskList = Arrays.asList(task1, task2, task3);

        long startTime = System.currentTimeMillis();

        // Returns the result of the fastest callable. (task2 in this case)
        String result = executorService.invokeAny(taskList);

        System.out.println(result + " after " + (System.currentTimeMillis() - startTime) + "ms");

        executorService.shutdown();
    }
}
