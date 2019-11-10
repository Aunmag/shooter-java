package aunmag.shooter.core.utilities;

public class Timer {

    public final TimeFlow time;
    public float start = 0.0f;
    public float duration;

    public Timer(TimeFlow time, float duration) {
        this.time = time;
        this.duration = duration;
        next();
    }

    public void next(boolean isDoneMustBe) {
        if (isDone() == isDoneMustBe) {
            next();
        }
    }

    public void next() {
        start = (float) time.getCurrent();
    }

    public boolean isDone() {
        return getPassed() >= duration;
    }

    public float getPassed() {
        return (float) time.getCurrent() - start;
    }

    public float getRemaining() {
        return duration - getPassed();
    }

    public float getProgress() {
        return getPassed() / duration;
    }

    public float getProgressLimited() {
        return UtilsMath.limit(getProgress(), 0, 1);
    }

}
