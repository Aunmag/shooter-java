package aunmag.shooter.core.utilities;

public class FrameRate {

    private int frequency;
    private double delta = 0.0;
    private double deltaMin = 0.0;
    private double deltaMax = 1.0;
    private double lastTickTime = 0.0;

    public FrameRate(int frequency) {
        setFrequency(frequency);
    }

    public boolean tryTick(double timeCurrent) {
        delta = timeCurrent - lastTickTime;

        boolean isNow = delta >= deltaMin;

        if (isNow) {
            lastTickTime = timeCurrent;

            if (delta > deltaMax) {
                delta = deltaMax;
            }
        } else {
            delta = 0;
        }

        return isNow;
    }

    /* Setters */

    public void setFrequency(int frequency) {
        this.frequency = frequency;

        if (frequency == 0) {
            deltaMin = 0.0;
        } else {
            deltaMin = 1.0 / frequency;
        }
    }

    public void setDeltaMax(double deltaMax) {
        this.deltaMax = deltaMax;
    }

    /* Getters */

    public int getFrequency() {
        return frequency;
    }

    public double getDelta() {
        return delta;
    }

    public double getDeltaMin() {
        return deltaMin;
    }

    public double getDeltaMax() {
        return deltaMax;
    }

    public double getLastTickTime() {
        return lastTickTime;
    }

}
