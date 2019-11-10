package aunmag.shooter.core.utilities;

public class FluidValue {

    public final Timer timer;
    public float source = 0;
    public float target = 0;
    public float tensity;

    public FluidValue(TimeFlow time, float duration) {
        this(time, duration, 1);
    }

    public FluidValue(TimeFlow time, float duration, float tensity) {
        this.timer = new Timer(time, duration);
        this.tensity = tensity;
    }

    public void set(float target) {
        source = get();
        this.target = target;
        timer.next();
    }

    public void set(float target, float duration) {
        set(target);
        timer.duration = duration;
    }

    public float get() {
        return (float) (
            source + (target - source) * Math.pow(timer.getProgressLimited(), tensity)
        );
    }

    public boolean isTargetReached() {
        return timer.isDone();
    }

}
