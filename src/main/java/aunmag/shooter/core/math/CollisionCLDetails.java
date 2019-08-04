package aunmag.shooter.core.math;

import org.joml.Vector2f;

/**
 * TODO: This is new. Complete it
 */
public class CollisionCLDetails {

    public final float deviation;
    public final Vector2f intersection;

    public CollisionCLDetails(CollisionCL collision) {
        var circle = collision.circle;
        var line = collision.line;

        var cos = (float) Math.cos(line.radians);
        var sin = (float) Math.sin(line.radians);

        intersection = new Vector2f(line.position);

        var distanceToCenter1 = intersection.distance(circle.position);
        intersection.sub(
                cos * distanceToCenter1,
                sin * distanceToCenter1
        );

        var distanceToCenter2 = intersection.distance(circle.position);
        var distanceToEdge = circle.radius - distanceToCenter2;
        intersection.sub(
                cos * distanceToEdge,
                sin * distanceToEdge
        );

        var deviation = distanceToCenter2 / circle.radius;

        var x1 = line.position.x - circle.position.x;
        var y1 = line.position.y - circle.position.y;
        var x2 = line.position.x - line.positionTail.x;
        var y2 = line.position.y - line.positionTail.y;
        var deviationRaw = x1 * y2 - y1 * x2;

        if (deviationRaw < 0) {
            deviation = -deviation;
        }

        this.deviation = deviation;
    }

}
