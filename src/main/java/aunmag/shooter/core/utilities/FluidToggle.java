package aunmag.shooter.core.utilities;

public class FluidToggle extends FluidValue {

    public static final int VALUE_MIN = 0;
    public static final int VALUE_MAX = 1;

    public FluidToggle(TimeFlow time, double duration) {
        super(time, duration);
    }

    public void on() {
        super.setTarget(VALUE_MAX);
    }

    public void off() {
        super.setTarget(VALUE_MIN);
    }

    public void toggle() {
        boolean toggleOn = getTarget() == VALUE_MIN;
        super.setTarget(toggleOn ? VALUE_MAX : VALUE_MIN);
    }

    /* Setters */

    public void setTarget(float target) {
        target = UtilsMath.limitNumber(target, VALUE_MIN, VALUE_MAX);
        super.setTarget(target);
    }

    /* Getters */

    public boolean isCompletelyOn() {
        return getCurrent() == VALUE_MAX;
    }

    public boolean isCompletelyOff() {
        return getCurrent() == VALUE_MIN;
    }

}
