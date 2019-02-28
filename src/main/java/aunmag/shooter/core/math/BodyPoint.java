package aunmag.shooter.core.math;

import aunmag.shooter.core.utilities.UtilsGraphics;
import aunmag.shooter.core.utilities.UtilsMath;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class BodyPoint {

    public final Vector4f color = new Vector4f(1.0f, 1.0f, 1.0f, 0.5f);
    public final Vector2f position;
    public float radians;

    public BodyPoint(float x, float y, float radians) {
        this.position = new Vector2f(x, y);
        this.radians = radians;
    }

    public void correctRadians() {
        radians = UtilsMath.correctRadians(radians);
    }

    public void render() {
        UtilsGraphics.setDrawColor(color);
    }

}
