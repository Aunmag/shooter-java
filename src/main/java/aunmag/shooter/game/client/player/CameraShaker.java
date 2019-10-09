package aunmag.shooter.game.client.player;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.utilities.FluidValue;
import aunmag.shooter.core.utilities.UtilsMath;

public final class CameraShaker {

    private static final float FACTOR = 0.125f;
    private static FluidValue radians;
    private static final float timeUp = 0.04f;
    private static final float timeDown = timeUp * 8;

    static {
        var flexDegree = 0.8f;
        radians = new FluidValue(Application.time, timeUp); // TODO: Use world time
        radians.setFlexDegree(flexDegree);
    }

    private CameraShaker() {}

    public static void shake(float force) {
        radians.timer.setDuration(timeUp);
        radians.setTarget(force * FACTOR);
    }

    public static void update() {
        radians.update();

        if (radians.isTargetReached() && radians.getTarget() != 0) {
            radians.timer.setDuration(timeDown);
            radians.setTarget(0);
        }

        if (radians.getCurrent() == 0.0f) {
            return;
        }

        var camera = Application.getCamera();
        var radiansCamera = camera.getRadians();
        var radiansShaker = radians.getCurrent();
        var radiansSum = UtilsMath.correctRadians(radiansCamera + radiansShaker);

        camera.setRadians(radiansSum);
        camera.mount.radians = radiansSum;
        camera.mount.apply();
    }

}
