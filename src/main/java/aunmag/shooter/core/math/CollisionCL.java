package aunmag.shooter.core.math;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class CollisionCL extends Collision {

    public final BodyCircle circle;
    public final BodyLine line;

    /**
     * TODO: This is new. Complete it
     */
    @Nullable private CollisionCLDetails details = null;

    public CollisionCL(BodyCircle circle, BodyLine line) {
        this.circle = circle;
        this.line = line;

        float x1 = circle.position.x - line.position.x;
        float y1 = circle.position.y - line.position.y;
        float x2 = circle.position.x - line.positionTail.x;
        float y2 = circle.position.y - line.positionTail.y;

        float differenceX = x2 - x1;
        float differenceY = y2 - y1;
        float radiusSquare = circle.radius * circle.radius;

        float a = differenceX * differenceX + differenceY * differenceY;
        float b = (x1 * differenceX + y1 * differenceY) * 2f;
        float c = x1 * x1 + y1 * y1 - radiusSquare;

        if (b > 0) {
            isTrue = c < 0;
        } else if (-b < 2 * a) {
            isTrue = a * c * 4 - b * b < 0;
        } else {
            isTrue = a + b + c < 0;
        }
    }

    /**
     * TODO: This is new. Complete it
     */
    public void resolveLineLength() {
        Vector2f intersection = provideDetails().intersection;
        line.position.set(intersection.x, intersection.y);
    }

    /**
     * TODO: This is new. Complete it
     */
    public CollisionCLDetails provideDetails() {
        if (details == null) {
            details = new CollisionCLDetails(this);
        }

        return details;
    }

}
