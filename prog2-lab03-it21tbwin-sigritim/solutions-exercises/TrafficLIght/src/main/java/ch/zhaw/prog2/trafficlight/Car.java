package ch.zhaw.prog2.trafficlight;

import java.util.Random;

class Car extends Thread {
    private final TrafficLight[] trafficLights;
    private int pos;

    public Car(String name, TrafficLight[] trafficLights) {
        super(name);
        this.trafficLights = trafficLights;
        pos = 0; // start at first light
        start();
    }

    public synchronized int position() {
        return pos;
    }

    private void gotoNextLight() {
        pos = ++pos % trafficLights.length;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            try {
                sleep(random.nextInt(500));
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            trafficLights[pos].passby();
            gotoNextLight();
        }
    }
}
