package aunmag.shooter.core.utilities;

import org.joml.Vector2f;

// TODO: Do not inherit from `Operative`
public class Quad extends Operative {

    public final Vector2f position;
    private float sizeX;
    private float sizeY;
    private float diagonal;

    public Quad(float sizeX, float sizeY) {
        this(0, 0, sizeX, sizeY);
    }

    public Quad(float x, float y, float sizeX, float sizeY) {
        this.position = new Vector2f(x, y);
        setSize(sizeX, sizeY);
    }

    public boolean contains(float x, float y) {
        return UtilsMath.inRange(x, position.x, position.x + sizeX)
            && UtilsMath.inRange(y, position.y, position.y + sizeY);
    }

    public void setSize(float sizeX, float sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        diagonal = (float) Math.sqrt(sizeX * sizeX + sizeY * sizeY);
    }

    public void setPositionCenteredBy(float x, float y) {
        position.x = x - getCenterX();
        position.y = y - getCenterY();
    }

    public float getSizeX() {
        return sizeX;
    }

    public float getSizeY() {
        return sizeY;
    }

    public float getMaxSide() {
        return Float.max(sizeX, sizeY);
    }

    public float getCenterX() {
        return sizeX / 2;
    }

    public float getCenterY() {
        return sizeY / 2;
    }

    public float getDiagonal() {
        return diagonal;
    }

    public float getAspectRatio() {
        return sizeX / sizeY;
    }

}
