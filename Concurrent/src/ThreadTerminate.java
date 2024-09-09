    import java.util.concurrent.atomic.AtomicBoolean;

class ThreadTerminate {
    public static void main(String[] args) {
        System.out.println("START: main");
        SimpleThreadEndless fiji = new SimpleThreadEndless("Fiji");
        fiji.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted main; " + e.getMessage());
        }
        fiji.terminate();
        System.out.println("DONE: main");
    }


    static class SimpleThreadEndless extends Thread {
        AtomicBoolean doContinue = new AtomicBoolean(true);

        public SimpleThreadEndless(String name) {
            super(name);
        }

        public void terminate() {
            doContinue.set(false);
            this.interrupt();
        }

        public void run() {
            System.out.println("START: " + getName());
            try {
                while (doContinue.get()) {
                    System.out.println("Thread " + getName());
                    Thread.sleep(20_000);
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted " + getName() + ": " + e.getMessage());
            }
            System.out.println("DONE: " + getName());
        }
    }
}





