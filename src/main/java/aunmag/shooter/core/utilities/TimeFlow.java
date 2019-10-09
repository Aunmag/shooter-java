package aunmag.shooter.core.utilities;

public class TimeFlow {

    private double current = 0.0;
    private double delta = 0.0;
    private double speed = 1.0;

    public void add(double add, boolean useSpeed) {
        delta = add;

        if (useSpeed) {
            delta *= speed;
        }

        current += delta;
    }

    public double getCurrent() {
        return current;
    }

    public double getDelta() {
        return delta;
    }

    public double getSpeed() {
        return speed;
    }

    public void setCurrent(double current, boolean doRecalculateDelta) {
        if (doRecalculateDelta) {
            delta = current - this.current;
        } else {
            delta = 0;
        }

        this.current = current;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

}
