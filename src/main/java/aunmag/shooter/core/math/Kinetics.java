package aunmag.shooter.core.math;

import org.joml.Vector2f;

public class Kinetics {

    public float mass;
    public final Vector2f velocity;
    public float spin = 0;
    public float restitution = 0.2f;
    public float restrictionFactor = 4.0f;
    public float restrictionFactorSpin = 8.0f;

    public Kinetics(float mass) {
        this(mass, 0, 0);
    }

    public Kinetics(float mass, float velocityX, float velocityY) {
        this.mass = mass;
        this.velocity = new Vector2f(velocityX, velocityY);
    }

    public void update(float delta) {
        velocity.mul(1 - restrictionFactor * delta);
        spin *= 1 - restrictionFactorSpin * delta;
    }

    public void addEnergy(float x, float y, float spin, float delta) {
        x *= restrictionFactor * delta * mass;
        y *= restrictionFactor * delta * mass;
        spin *= restrictionFactorSpin * delta * mass;

        push(x, y, spin);
    }

    public void push(float x, float y, float spin) {
        velocity.x += compensateMomentum(x);
        velocity.y += compensateMomentum(y);
        this.spin += compensateMomentum(spin);
    }

    public float compensateMomentum(float momentum) {
        return momentum / mass;
    }

    public float getMomentum() {
        return velocity.length() * mass;
    }

    public static void interact(Kinetics a, Kinetics b, Vector2f direction) {
        var velocity = new Vector2f(b.velocity).sub(a.velocity).dot(direction);

        if (velocity >= 0 || Float.isNaN(velocity) || Float.isInfinite(velocity)) {
            return;
        }

        var massA = 1 / a.mass;
        var massB = 1 / b.mass;
        var restitution = Math.min(a.restitution, b.restitution);
        var impulse = -(1 + restitution) * velocity / (massA + massB);

        a.velocity.sub(new Vector2f(direction).mul(impulse * massA));
        b.velocity.add(new Vector2f(direction).mul(impulse * massB));
    }

    public static void interact(Kinetics a, Kinetics b) {
        interact(a, b, new Vector2f(b.velocity).add(a.velocity).normalize());
    }

    public static void interact(
            Kinetics a,
            Kinetics b,
            Vector2f positionA,
            Vector2f positionB
    ) {
        interact(a, b, new Vector2f(positionB).sub(positionA).normalize());
    }

}
