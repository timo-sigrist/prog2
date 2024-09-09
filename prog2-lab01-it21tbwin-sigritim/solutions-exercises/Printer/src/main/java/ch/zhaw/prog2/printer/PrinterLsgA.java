package ch.zhaw.prog2.printer;


public class PrinterLsgA {

    // test program
    public static void main(String[] arg) {
        PrinterThread a = new PrinterThread("PrinterA", '.', 0);
        PrinterThread b = new PrinterThread("PrinterB", '*', 0);
        a.start();
        b.start();
        b.run(); // wie kann das abgefangen werden?
    }


    private static class PrinterThread extends Thread {
        char symbol;
        int sleepTime;

        public PrinterThread(String name, char symbol, int sleepTime) {
            super(name);
            this.symbol = symbol;
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            if (this != currentThread()) {
                throw new IllegalStateException("run() must not be called directly");
            }
            System.out.println(getName() + " run started...");
            for (int i = 1; i < 100; i++) {
                System.out.print(symbol);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println('\n' + getName() + " run ended.");
        }
    }

}
