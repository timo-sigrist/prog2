package ch.zhaw.prog2.bridge;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficControllerB extends TrafficController {

    private boolean bridgeOccupied = false;
    private Lock mutex = new ReentrantLock();
    private Condition enterLeft = mutex.newCondition();
    private Condition enterRight = mutex.newCondition();

    public void enterLeft() {
        mutex.lock();
        try {
            while(bridgeOccupied) {
                enterLeft.await();
            }
            bridgeOccupied = true;
        } catch (InterruptedException e) {
            System.err.println("Interrupt: " + e.getMessage());
        } finally {
            mutex.unlock();
        }
    }

    public void enterRight() {
        mutex.lock();
        try {
            while(bridgeOccupied) {
                enterRight.await();
            }
            bridgeOccupied = true;
        } catch (InterruptedException e) {
            System.err.println("Interrupt: " + e.getMessage());
        } finally {
            mutex.unlock();
        }
    }

    public void leaveLeft() {
        mutex.lock();
        try {
            bridgeOccupied = false;
            enterLeft.signal(); // wake-up car on opposite side
        } finally {
            mutex.unlock();
        }
    }

    public void leaveRight() {
        mutex.lock();
        try {
            bridgeOccupied = false;
            enterRight.signal(); // wake-up car on opposite side
        } finally {
            mutex.unlock();
        }
    }

}
