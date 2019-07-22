package aunmag.shooter.core.math;

import aunmag.shooter.core.utilities.UtilsMath;

public class CollisionCC extends Collision {

    public static final float EXTRA_RESOLVE_DISTANCE = 0.0001f;

    public final BodyCircle a;
    public final BodyCircle b;
    public final float distanceSquared;
    public final float distanceMin;

    public CollisionCC(BodyCircle a, BodyCircle b) {
        this.a = a;
        this.b = b;

        distanceSquared = a.position.distanceSquared(b.position);
        distanceMin = a.radius + b.radius;
        isTrue = distanceSquared < distanceMin * distanceMin;
    }

    public void resolve() {
        if (!isTrue) {
            return;
        }

        var distance = (distanceMin - (float) Math.sqrt(distanceSquared))
                / 2.0f
                + EXTRA_RESOLVE_DISTANCE;

        var direction = UtilsMath.angle(a.position, b.position);
        var x = distance * (float) Math.cos(direction);
        var y = distance * (float) Math.sin(direction);

        a.position.add(x, y);
        b.position.sub(x, y);
    }

}
