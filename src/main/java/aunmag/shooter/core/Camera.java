package aunmag.shooter.core;

import aunmag.shooter.core.audio.AudioMaster;
import aunmag.shooter.core.basics.BaseObject;
import aunmag.shooter.core.utilities.Mount;
import aunmag.shooter.core.utilities.UtilsMath;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera extends BaseObject {

    public static final int ZOOM_MIN = 1;
    public static final int ZOOM_MAX = 8;

    private Matrix4f viewMatrix = new Matrix4f();
    private float distanceView;
    private float scaleWindow;
    private float scaleZoom = ZOOM_MIN * 2;
    private float scaleFull;
    public final Mount mount;

    Camera() {
        super(new Vector2f(0, 0), 0);
        setDistanceView(40);
        mount = new Mount(getPosition(), null);
    }

    public void update() {
        float radians = UtilsMath.correctRadians(getRadians() - UtilsMath.PIx0_5);

        viewMatrix = new Matrix4f(Application.getWindow().projection);
        viewMatrix.rotateZ(-radians);
        viewMatrix.scale(scaleFull);
        viewMatrix.translate(-getPosition().x(), -getPosition().y(), 0);

        Vector3f orientation = new Vector3f(0, 1, 0);
        Quaternionf quaternion = new Quaternionf(0, 0, 0);
        quaternion.set(0, 0, 0);
        quaternion.rotateZ(radians);
        orientation.rotate(quaternion);
        AudioMaster.setListenerPosition(
                getPosition().x(),
                getPosition().y(),
                0,
                orientation
        );
    }

    private void updateScaleFull() {
        scaleFull = scaleWindow * scaleZoom;
    }

    public Vector2f calculateViewPosition(float x, float y) {
        Vector3f viewPosition = new Vector3f(x, y, 0);
        viewPosition.mulPosition(viewMatrix);
        return new Vector2f(viewPosition.x, viewPosition.y);
    }

    public Matrix4f calculateViewProjection(float x, float y, float radians) {
        Matrix4f projection = new Matrix4f(viewMatrix);
        projection.translate(x, y, 0);
        projection.rotateZ(radians);
        return projection;
    }

    /* Setters */

    public void setDistanceView(float distanceView) {
        this.distanceView = distanceView;
        scaleWindow = Application.getWindow().getDiagonal() / distanceView;
        updateScaleFull();
    }

    public void setScaleZoom(float scaleZoom) {
        this.scaleZoom = UtilsMath.limitNumber(scaleZoom, ZOOM_MIN, ZOOM_MAX);
        updateScaleFull();
    }

    /* Getters */

    public float getDistanceView() {
        return distanceView;
    }

    public float getScaleWindow() {
        return scaleWindow;
    }

    public float getScaleZoom() {
        return scaleZoom;
    }

    public float getScaleFull() {
        return scaleFull;
    }

}
