package aunmag.shooter.core.utilities;

public class FluidToggle extends FluidValue {

    public static final int VALUE_MIN = 0;
    public static final int VALUE_MAX = 1;

    public FluidToggle(TimeFlow time, float duration) {
        super(time, duration);
    }

    public FluidToggle(TimeFlow time, float duration, float tensity) {
        super(time, duration, tensity);
    }

    public void turnOn() {
        if (!isTurningOn()) {
            set(VALUE_MAX);
        }
    }

    public void turnOff() {
        if (!isTurningOff()) {
            set(VALUE_MIN);
        }
    }

    public boolean isTurningOn() {
        return target == VALUE_MAX;
    }

    public boolean isTurningOff() {
        return target == VALUE_MIN;
    }

    public boolean isTurnedOn() {
        return isTargetReached() && isTurningOn();
    }

    public boolean isTurnedOff() {
        return isTargetReached() && isTurningOff();
    }

}
