package aunmag.shooter.core;

import aunmag.shooter.core.utilities.Mount;
import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.UtilsMath;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera extends Operative {

    private Matrix4f viewMatrix = new Matrix4f();
    public final Vector2f position = new Vector2f(0, 0);
    public float radians = 0;
    public float scale = 15;
    public final Mount mount = new Mount(position, null);

    @Override
    public void update() {
        var angle = UtilsMath.correctRadians(radians - UtilsMath.PIx0_5);

        viewMatrix = new Matrix4f(Context.main.getWindow().projection)
                .rotateZ(-angle)
                .scale(getPixelsScale())
                .translate(-position.x, -position.y, 0);

        Context.main.getListener().setPosition(position.x, position.y, angle);
    }

    public Vector2f project(float x, float y) {
        var projected = new Vector3f(x, y, 0).mulPosition(viewMatrix);
        return new Vector2f(projected.x, projected.y);
    }

    public Matrix4f toViewProjection(float x, float y, float angle) {
        return new Matrix4f(viewMatrix).translate(x, y, 0).rotateZ(angle);
    }

    public float toMeters(float distance) {
        return distance / getPixelsScale();
    }

    public float toPixels(float distance) {
        return distance * getPixelsScale();
    }

    public float getPixelsScale() {
        return Context.main.getWindow().getDiagonal() / scale;
    }

}
