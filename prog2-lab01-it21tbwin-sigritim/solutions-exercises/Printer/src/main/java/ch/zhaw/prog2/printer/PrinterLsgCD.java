package ch.zhaw.prog2.printer;


public class PrinterLsgCD {

    // test program
    public static void main(String[] arg) {
        PrinterRunnable a = new PrinterRunnable('.', 0); // different if you change the sleep
                                           // time?
        PrinterRunnable b = new PrinterRunnable('*', 0); // different if you change the sleep
                                           // time?
        Thread t1 = new Thread(a, "PrinterA");
        Thread t2 = new Thread(b, "PrinterB");
        t1.start();
        t2.start();
        // d)
        try {
            t1.join(); // wait for t1 to die
            t2.join(); // wait for t2 to die
        } catch (InterruptedException e) {
            System.err.println("Join interrupted");
        }
    }

    private static class PrinterRunnable implements Runnable {
        char symbol;
        int sleepTime;

        public PrinterRunnable(char symbol, int sleepTime) {
            this.symbol = symbol;
            this.sleepTime = sleepTime;
        }

        public void run() {
            Thread current = Thread.currentThread();
            System.out.println(current.getName() + " run started...");
            for (int i = 1; i < 100; i++) {
                System.out.print(symbol);
                // c)
                Thread.yield(); // release CPU and give other thread a chance
//                try {
//                    Thread.sleep(sleepTime);
//                } catch (InterruptedException e) {
//                    System.out.println(e.getMessage());
//                }
            }
            System.out.println('\n' + current.getName() + " run ended.");
        }
    }

}
