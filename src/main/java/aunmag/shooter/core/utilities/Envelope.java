package aunmag.shooter.core.utilities;

public class Envelope {

    private final float attack;
    private final float release;
    private final FluidValue value;

    public Envelope(float attack, float release, TimeFlow time) {
        this(attack, release, 1, time);
    }

    public Envelope(float attack, float release, float tensity, TimeFlow time) {
        this.attack = attack;
        this.release = release;
        this.value = new FluidValue(time, attack, tensity);
    }

    public void update() {
        if (value.isTargetReached() && value.target != 0) {
            value.set(0, release);
        }
    }

    public void start(float value) {
        this.value.set(value + this.value.get(), attack);
    }

    public float getValue() {
        return value.get();
    }

}
