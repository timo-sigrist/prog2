package ch.zhaw.prog2.bridge;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficControllerA extends TrafficController {

    private boolean bridgeOccupied = false;
    private Lock mutex = new ReentrantLock();
    private Condition enterBridge = mutex.newCondition();

    public void enterLeft() {
        mutex.lock();
        try {
            while(bridgeOccupied) {
                enterBridge.await();
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
                enterBridge.await();
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
            enterBridge.signal();
        } finally {
            mutex.unlock();
        }
    }

    public void leaveRight() {
        mutex.lock();
        try {
            bridgeOccupied = false;
            enterBridge.signal();
        } finally {
            mutex.unlock();
        }
    }

}
