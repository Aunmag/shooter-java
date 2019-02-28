package aunmag.shooter.core.math;

import aunmag.shooter.core.utilities.UtilsGraphics;
import aunmag.shooter.core.utilities.UtilsMath;
import org.joml.Vector2f;

public class BodyLine extends BodyPoint {

    public final Vector2f positionTail;

    public BodyLine(float x1, float y1, float x2, float y2) {
        super(x1, y1, 0);
        positionTail = new Vector2f(x2, y2);
        updateRadians();
    }

    public void pullUpHead() {
        position.set(positionTail);
    }

    public void pullUpTail() {
        positionTail.set(position);
    }

    public void updateRadians() {
        radians = UtilsMath.calculateRadiansBetween(position, positionTail);
    }

    @Override
    public void render() {
        super.render();
        UtilsGraphics.drawLine(
                position.x,
                position.y,
                positionTail.x,
                positionTail.y,
                true
        );
    }

}
