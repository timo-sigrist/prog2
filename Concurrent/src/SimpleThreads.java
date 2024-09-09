import static java.lang.Math.random;


class SimpleThreads {

    public static void main(String[] args) {
        System.out.println("START: " + Thread.currentThread().getName());

        new SimpleThread("Java").start(); // short form to run Thread

        Thread fiji = new SimpleThread();       // extended form to run Thread
        fiji.setName("Fiji");
        fiji.start();

        System.out.println("DONE: " + Thread.currentThread().getName());
    }


    static class SimpleThread extends Thread {
        // Constructors
        public SimpleThread() { }
        public SimpleThread(String name) {
            super(name);
        }

        public void run() {
            System.out.println("START: " + getName());
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println(i + " " + getName());
                    Thread.sleep((int)(random() * 1000));
                }
           } catch (InterruptedException e) {  // required because of blocking method sleep()
                System.err.println("Interrupted: " + e.getMessage());
            }
            System.out.println("DONE: " + getName());
        }
    }
}




