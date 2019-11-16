package aunmag.shooter.core.utilities;

public class Envelope {

    private static int MIN_VALUE = 0;

    private final float attack;
    private final float sustain; // TODO: Use
    private final float release;
    private final FluidValue value;

    public Envelope(float attack, float sustain, float release, TimeFlow time) {
        this(attack, sustain, release, 1, time);
    }

    public Envelope(
        float attack,
        float sustain,
        float release,
        float tensity,
        TimeFlow time
    ) {
        this.attack = attack;
        this.sustain = sustain;
        this.release = release;
        this.value = new FluidValue(time, attack, tensity);
    }

    // TODO: Try to implement without update
    public void update() {
        if (value.isTargetReached() && value.target != MIN_VALUE) {
            if (value.isSame()) {
                value.set(MIN_VALUE, release);
            } else {
                value.set(value.get(), sustain);
            }
        }
    }

    public void start(float value) {
        this.value.set(value + this.value.get(), attack);
    }

    public float getValue() {
        return value.get();
    }

    // TODO: Use
    public boolean isDone() {
        return value.target == MIN_VALUE && value.isTargetReached();
    }

}
