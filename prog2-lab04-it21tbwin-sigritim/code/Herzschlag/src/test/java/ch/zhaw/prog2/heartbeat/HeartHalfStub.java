package ch.zhaw.prog2.heartbeat;

import ch.zhaw.prog2.heartbeat.parts.Valve;

public class HeartHalfStub extends Half {

    public HeartHalfStub() {
        super(Side.LEFT);
    }

    public void openAtrioventricularValve() throws Valve.IllegalValveStateException {
        throw new Valve.IllegalValveStateException();
    }
    public void closeSemilunarValve() throws Valve.IllegalValveStateException {
        throw new Valve.IllegalValveStateException();
    }
    public void  relaxAtrium() {}
    public void relaxVentricle() {}
    public void closeAtrioventricularValve() {}
    public void openSemilunarValve() {}
    public void contractVentricle() {}
    public void contractAtrium() {}
}
