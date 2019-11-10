package aunmag.shooter.core.utilities;

public class TimerRandomized extends Timer {

    private float deviationFactor;

    public TimerRandomized(TimeFlow time, float duration, float deviationFactor) {
        super(time, duration);
        this.deviationFactor = deviationFactor;
    }

    @Override
    public void next() {
        start = (float) time.getCurrent() + UtilsRandom.deviation(
            0, duration * deviationFactor
        );
    }

}
