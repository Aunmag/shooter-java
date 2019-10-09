package aunmag.shooter.core.utilities;

public class Timer {

    public final TimeFlow time;
    private double target = 0.0;
    private double duration = 0.0;
    private double durationDeviationFactor = 0.0;
    private double durationCurrent = 0.0;

    public Timer(TimeFlow time, double duration) {
        this(time, duration, 0.0);
    }

    public Timer(
            TimeFlow time,
            double duration,
            double durationDeviationFactor
    ) {
        this.time = time;
        this.durationDeviationFactor = durationDeviationFactor;

        target += time.getCurrent(); // TODO: Why do I need it?
        setDuration(duration);
    }

    public void next(boolean isDoneMustBe) {
        if (isDone() == isDoneMustBe) {
            next();
        }
    }

    public void next() {
        updateDurationCurrent();
        setTarget(durationCurrent + time.getCurrent());
    }

    private void updateDurationCurrent() {
        durationCurrent = UtilsMath.randomizeFlexibly(
                (float) duration,
                (float) (duration * durationDeviationFactor)
        );
    }

    public double calculateIsDoneRatio() {
        return getPassed() / duration;
    }

    public void setDuration(double duration) {
        target -= durationCurrent;

        this.duration = duration;
        updateDurationCurrent();

        target += durationCurrent;
    }

    public void setDurationDeviationFactor(double durationDeviationFactor) {
        this.durationDeviationFactor = durationDeviationFactor;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getDuration() {
        return duration;
    }

    public double getDurationDeviationFactor() {
        return durationDeviationFactor;
    }

    public double getDurationCurrent() {
        return durationCurrent;
    }

    public double getInitial() {
        return target - duration;
    }

    public double getPassed() {
        return time.getCurrent() - getInitial();
    }

    public double getRemain() {
        return target - time.getCurrent();
    }

    public double getTarget() {
        return target;
    }

    public boolean isDone() {
        return time.getCurrent() >= target;
    }

}
