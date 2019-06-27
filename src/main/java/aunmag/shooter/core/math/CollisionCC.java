package aunmag.shooter.core.math;

import aunmag.shooter.core.utilities.UtilsMath;

public class CollisionCC extends Collision {

    public final BodyCircle a;
    public final BodyCircle b;
    public final float distance;
    public final float distanceToBeCollision;

    public CollisionCC(BodyCircle a, BodyCircle b) {
        this.a = a;
        this.b = b;

        distanceToBeCollision = a.radius + b.radius;
        distance = a.position.distance(b.position);
        isTrue = distance < distanceToBeCollision;
    }

    public void resolve() {
        if (!isTrue) {
            return;
        }

        float moveDistance = (distanceToBeCollision - distance) / 2f;
        float moveRadians = UtilsMath.angle(a.position, b.position);
        float moveX = moveDistance * (float) Math.cos(moveRadians);
        float moveY = moveDistance * (float) Math.sin(moveRadians);

        a.position.add(moveX, moveY);
        b.position.sub(moveX, moveY);
    }

}
