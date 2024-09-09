package concurrency2;

public class SimpleTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Inside thread : " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());
        }
        System.out.println("Task ends");
    }
}
