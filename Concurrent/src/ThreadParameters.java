import static java.lang.Math.random;


class ThreadParameters {
    public static void main(String[] args) {
        System.out.println("START: " + Thread.currentThread().getName());

        new ParamThread("Java", 10).start();
        new ParamThread("Fiji", 20).start();

        System.out.println("DONE: " + Thread.currentThread().getName());
    }


    static class ParamThread extends Thread {

        private final int iterations;

        public ParamThread(String name, int iterations) {
            super(name);
            this.iterations = iterations;
        }

        public void run() {
            System.out.println("START: " + getName());
            try {
                for (int i = 0; i < iterations; i++) {
                    System.out.println(i + " " + getName());
                    Thread.sleep((int)(random() * 1000));
                }
            } catch (InterruptedException e) { // required because of blocking method sleep()
                System.err.println("Interrupted: " + e.getMessage());
            }
            System.out.println("DONE: " + getName());
        }
    }
}





