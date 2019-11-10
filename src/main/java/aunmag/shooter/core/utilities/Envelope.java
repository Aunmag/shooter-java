package aunmag.shooter.core.utilities;

public class Envelope {

    private final float attack;
    private final float release;
    private final FluidValue value;

    public Envelope(float attack, float release, TimeFlow time) {
        this(attack, release, 1, time);
    }

    public Envelope(float attack, float release, float bend, TimeFlow time) {
        this.attack = attack;
        this.release = release;
        this.value = new FluidValue(time, attack);
        this.value.setFlexDegree(bend);
    }

    public void update() {
        value.update();

        if (value.isTargetReached() && value.getTarget() != 0) {
            value.timer.duration = release;
            value.setTarget(0);
        }
    }

    public void start(float value) {
        this.value.timer.duration = attack;
        this.value.setTarget(value + this.value.getCurrent());
    }

    public float getValue() {
        return value.getCurrent();
    }

}
