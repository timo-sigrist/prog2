import static java.lang.Math.random;


public class ThreadJoin {
    public static void main(String[] args) {
        System.out.println("START: main");

        Thread java = new SimpleThread("Java");
        Thread fiji = new SimpleThread("Fiji");
        java.start();
        fiji.start();

        System.out.println("Wait for threads..");
        try {
            java.join(); // blocks until java ends
            fiji.join(); // blocks until fiji ends
        } catch (InterruptedException e) {  // required because of blocking method join()
            System.out.println("Interrupted main: " + e.getMessage());
        }
        // or is already terminated
        System.out.println("DONE: main");
    }


    // SimpleThread from first example
    static class SimpleThread extends Thread {
        public SimpleThread(String name) {
            super(name);
        }

        public void run() {
            System.out.println("START: " + getName());
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println(i + " " + getName());
                    Thread.sleep((int) (random() * 1000));
                }
            } catch (InterruptedException e) { // required because of blocking method sleep()
                System.err.println("Interrupted " + getName() + ": " + e.getMessage());
            }
            System.out.println("DONE: " + getName());
        }
    }
}






