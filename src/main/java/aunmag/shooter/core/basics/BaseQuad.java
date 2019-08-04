package aunmag.shooter.core.basics;

import aunmag.shooter.core.utilities.UtilsMath;
import org.joml.Vector2f;

public class BaseQuad extends BaseObject {

    private float width;
    private float height;
    private float centerX;
    private float centerY;
    private float diagonal;
    private float aspectRatio;

    public BaseQuad(float width, float height) {
        this(new Vector2f(0, 0), width, height);
    }

    public BaseQuad(float x, float y, float width, float height) {
        this(new Vector2f(x, y), width, height);
    }

    public BaseQuad(Vector2f position, float width, float height) {
        super(position, 0);
        setSize(width, height);
    }

    public boolean calculateIsPointInside(float testX, float testY) {
        var x = getPosition().x();
        var y = getPosition().y();

        return UtilsMath.inRange(testX, x, x + width)
            && UtilsMath.inRange(testY, y, y + height);
    }
    protected void setSize(float width, float height) {
        this.width = width;
        this.height = height;

        centerX = width / 2f;
        centerY = height / 2f;

        diagonal = (float) Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));

        aspectRatio = width / height;
    }

    public void setPositionCenteredBy(float x, float y) {
        getPosition().set(x - getCenterX(), y - getCenterY());
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getMaxSide() {
        return Float.max(width, height);
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getDiagonal() {
        return diagonal;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

}
