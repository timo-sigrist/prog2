package ch.zhaw.prog2.circularbuffer;

public class GuardedCircularBuffer<T> implements Buffer<T> {
    private CircularBuffer<T> buffer;

    public GuardedCircularBuffer(Class<T> clazz, int bufferSize) {
        buffer = new CircularBuffer<>(clazz, bufferSize);
    }

    public synchronized boolean put(T item) throws InterruptedException {
        while (buffer.full()) {
            wait();
        }
        boolean retVal = buffer.put(item);
        notifyAll();
        return retVal;

    }

    public synchronized T get() throws InterruptedException {
        while (buffer.empty()) {
            wait();
        }
        T item = buffer.get();
        notifyAll();
        return item;

    }

    public synchronized void printBufferSlots() {
        buffer.printBufferSlots();
    }

    public synchronized void printBufferContent() {
        buffer.printBufferContent();
    }

    public synchronized boolean empty() {
        return buffer.empty();
    }

    public synchronized boolean full() {
        return buffer.full();
    }

    public synchronized int count() {
        return buffer.count();
    }
}
