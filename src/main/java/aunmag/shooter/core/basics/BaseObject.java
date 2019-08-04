package aunmag.shooter.core.basics;

import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.UtilsMath;
import org.joml.Vector2f;

public class BaseObject extends Operative {

    private final Vector2f position;
    private float radians;

    public BaseObject(float x, float y, float radians) {
        this(new Vector2f(x, y), radians);
    }

    public BaseObject(Vector2f position, float radians) {
        this.position = position;
        this.radians = radians;
    }

    public void correctRadians() {
        radians = UtilsMath.correctRadians(radians);
    }

    public Vector2f copyPosition() {
        return new Vector2f(position);
    }

    public void setRadians(float radians) {
        this.radians = radians;
    }

    public void addRadians(float radians) {
        setRadians(getRadians() + radians);
    }

    public void subRadians(float radians) {
        setRadians(getRadians() - radians);
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getRadians() {
        return radians;
    }

}
