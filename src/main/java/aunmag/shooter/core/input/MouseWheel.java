package aunmag.shooter.core.input;

import aunmag.shooter.core.Application;

public class MouseWheel {

    public static final float VELOCITY_SMOOTH_MIN = 0.001f;
    public float smoothness = 8f;
    private float velocity = 0;
    private float velocitySmooth = 0;

    public void update() {
        velocity = 0;

        if (Math.abs(velocitySmooth) > VELOCITY_SMOOTH_MIN) {
            float delta = (float) Application.time.getDelta();
            velocitySmooth -= velocitySmooth * smoothness * delta;
        } else {
            velocitySmooth = 0;
        }
    }

    public void onScroll(long window, double x, double y) {
        velocity = (float) y;
        velocitySmooth += velocity;
    }

    /* Getters */

    public float getVelocity() {
        return velocity;
    }

    public float getVelocitySmooth() {
        return velocitySmooth;
    }

}
