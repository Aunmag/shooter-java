package aunmag.shooter.core.math;

import org.joml.Vector2f;

/**
 * TODO: This is new. Complete it
 */
public class CollisionCLDetails {

    public final float deviation;
    public final Vector2f intersection;

    public CollisionCLDetails(CollisionCL collision) {
        BodyCircle circle = collision.circle;
        BodyLine line = collision.line;

        float cos = (float) Math.cos(line.radians);
        float sin = (float) Math.sin(line.radians);

        intersection = new Vector2f(line.position);

        float distanceToCenter1 = intersection.distance(circle.position);
        intersection.sub(
                cos * distanceToCenter1,
                sin * distanceToCenter1
        );

        float distanceToCenter2 = intersection.distance(circle.position);
        float distanceToEdge = circle.radius - distanceToCenter2;
        intersection.sub(
                cos * distanceToEdge,
                sin * distanceToEdge
        );

        float deviation = distanceToCenter2 / circle.radius;

        float x1 = line.position.x - circle.position.x;
        float y1 = line.position.y - circle.position.y;
        float x2 = line.position.x - line.positionTail.x;
        float y2 = line.position.y - line.positionTail.y;
        float deviationRaw = x1 * y2 - y1 * x2;

        if (deviationRaw < 0) {
            deviation = -deviation;
        }

        this.deviation = deviation;
    }

}
