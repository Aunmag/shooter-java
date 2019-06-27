package aunmag.shooter.core.utilities;

public class Pulse {

    private final FluidToggle pulse;

    public Pulse(TimeFlow time, double rate) {
        pulse = new FluidToggle(time, rate);
    }

    public void update() {
        pulse.update();

        if (pulse.isTargetReached()) {
            pulse.toggle();
        }
    }

    public float getValue() {
        return pulse.getCurrent();
    }

    public float getValue(float bound) {
        return getValue() * bound + bound / 2;
    }

}
