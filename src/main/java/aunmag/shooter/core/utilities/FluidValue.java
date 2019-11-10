package aunmag.shooter.core.utilities;

public class FluidValue {

    public final Timer timer;
    private float initial = 0;
    private float current = 0;
    private float target = 0;
    private float flexDegree = 1;

    public FluidValue(TimeFlow time, float duration) {
        timer = new Timer(time, duration);
    }

    public void update() {
        if (isTargetReached()) {
            return;
        }

        if (timer.isDone()) {
            reachTargetNow();
        } else {
            var progress = Math.pow(timer.getProgressLimited(), flexDegree);
            var valueIncrease = getValueRange() * progress;
            current = (float) (initial + valueIncrease);
        }
    }

    public void reachTargetNow() {
        current = target;
    }

    public void setTarget(float target) {
        if (target != this.target) {
            initial = this.current;
            this.target = target;
            timer.next();
        }
    }

    public void setFlexDegree(float flexDegree) {
        this.flexDegree = flexDegree;
    }

    public float getCurrent() {
        return current;
    }

    public float getTarget() {
        return target;
    }

    public float getValueRange() {
        return target - initial;
    }

    public boolean isTargetReached() {
        return current == target;
    }

}
