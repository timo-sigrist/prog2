package ch.zhaw.prog2.circularbuffer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CircBufferSimulation {
    public static void main(String[] args) {
        final int capacity = 15; // Number of buffer items
        final int prodCount = 1; // Number of producer threads
        final int consCount = 1; // Number of consumer threads
        final int maxProdTime = 500; // max. production time for one item
        final int maxConsTime = 500; // max. consumption time for one item

        try {
            Buffer<String> buffer = new GuardedCircularBuffer<>(String.class, capacity);

            // start consumers
            Consumer[] consumers = new Consumer[consCount];
            for (int i = 0; i < consCount; i++) {
                consumers[i] = new Consumer("Consumer_" + i, buffer, maxConsTime);
                consumers[i].start();
            }
            // start producers
            Producer[] producers = new Producer[prodCount];
            for (int i = 0; i < prodCount; i++) {
                producers[i] = new Producer("Producer_" + i, buffer, maxProdTime);
                producers[i].start();
            }

            // print live buffer status
            ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);
            // print occupied slots of buffer every second
            scheduled.scheduleAtFixedRate(buffer::printBufferSlots, 1, 1, TimeUnit.SECONDS);
            // print content of buffer every 5 seconds
            scheduled.scheduleAtFixedRate(buffer::printBufferContent, 5,5, TimeUnit.SECONDS);


            System.out.println("Press Enter to terminate");
            System.in.read();

            System.out.println("Shutting down ...");
            // shutdown producers
            for (Producer producer : producers) {
                producer.terminate();
            }
            // shutdown consumers
            for (Consumer consumer : consumers) {
                consumer.terminate();
            }
            // shutdown statistics
            scheduled.shutdown();
            System.out.println("Simulation ended.");

        } catch (Exception logOrIgnore) {
            System.out.println(logOrIgnore.getMessage());
        }
    }

    private static class Producer extends Thread {
        private volatile boolean running = true;
        private final Buffer<String> buffer;
        private final int maxProdTime;

        public Producer(String name, Buffer<String> buffer, int prodTime) {
            super(name);
            this.buffer = buffer;
            maxProdTime = prodTime;
        }

        public void terminate() {
            running = false;
        }

        @Override
        public void run() {
            running = true;
            int number = 1;
            try {
                while (running) {
                    buffer.put("#" + number++);
                    sleep((int) (100 + Math.random() * maxProdTime));
                }
            } catch (InterruptedException ex) {
                System.err.println("Interrupted in " + getName() + ": " + ex.getMessage());
            }
        }
    }

    private static class Consumer extends Thread {
        private volatile boolean running = true;
        private final Buffer<String> buffer;
        private final int maxConsTime;

        public Consumer(String name, Buffer<String> buffer, int consTime) {
            super(name);
            this.buffer = buffer;
            maxConsTime = consTime;
        }

        public void terminate() {
            running = false;
        }

        @Override
        public void run() {
            running = true;
            try {
                while (running) {
                    buffer.get();
                    Thread.sleep(100 + (int) (Math.random() * maxConsTime));
                }
            } catch (InterruptedException ex) {
                System.err.println("Interrupted in " + getName() + ": " + ex.getMessage());
            }
        }
    }

}
