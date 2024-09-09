import static java.lang.Math.random;


public class SimpleRunnables {

    public static void main(String[] args) {
        System.out.println("START: " + Thread.currentThread().getName());

        Runnable run = new SimpleRunnable();   // create instance of Runnable

        new Thread(run,"Java").start(); // short form to run Thread

        Thread fiji = new Thread(run);        // extended form to run Thread
        fiji.setName("Fiji");
        fiji.start();

        System.out.println("DONE: " + Thread.currentThread().getName());
    }


    static class SimpleRunnable implements Runnable {

        public void run() {
            String name = Thread.currentThread().getName();
            System.out.println("START: " + name);
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println(i + " " + name);
                    Thread.sleep((int)(random() * 1000));
                }
            } catch (InterruptedException e) {  // required because of blocking method sleep()
                System.err.println("Interrupted: " + e.getMessage());
            }
            System.out.println("DONE: " + name);
        }
    }
}


