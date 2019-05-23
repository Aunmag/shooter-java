package aunmag.shooter.game.client.player;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.input.Input;
import aunmag.shooter.core.utilities.UtilsGraphics;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.client.App;
import aunmag.shooter.game.environment.actor.Actor;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class Player {

    public static final float MOUSE_SENSITIVITY = 0.003f;
    public static final float MOUSE_SENSITIVITY_AIMING_FACTOR = 0.75f;
    public static final float ZOOM_VELOCITY = 1.5f;
    public static final float SCALE_MIN = 5.0f;
    public static final float SCALE_MAX = 30.0f;
    public static final float SCALE_DEFAULT = 15.0f;
    public static final float CAMERA_OFFSET_RATIO = 0.25f;

    @Nullable private Actor actor = null;
    @Nullable private Blackout blackout = null;
    private final Crosshair crosshair = new Crosshair();
    private boolean isAiming = false;

    public Player() {
        App.getCamera().scale = SCALE_DEFAULT;
    }

    public void updateInput() {
        if (actor == null) {
            return;
        }

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
            actor.control.walkForward();
        }

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
            actor.control.walkBack();
        }

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
            actor.control.walkLeft();
        }

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
            actor.control.walkRight();
        }

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            actor.control.sprint();
        }

        if (Input.mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
            actor.control.attack();
        }

        if (Input.mouse.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_2)) {
            isAiming = !isAiming;
        }

        if (isAiming) {
            actor.control.aim();
        }

        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_R) && actor.getHasWeapon()) {
            actor.getWeapon().magazine.reload();
        }

        actor.body.radians += getRotation();
        actor.body.correctRadians();

        Application.getCamera().scale = UtilsMath.limitNumber(
                Application.getCamera().scale - getZooming(),
                SCALE_MIN,
                SCALE_MAX
        );
    }

    public void updateCameraPosition() {
        if (actor == null) {
            return;
        }

        var camera = App.getCamera();
        var window = App.getWindow();
        var offset = camera.toMeters(
                window.getHeight()
                * CAMERA_OFFSET_RATIO
                * (1.0f + actor.isAiming.getCurrent())
        );

        camera.setRadians(actor.body.radians);
        camera.mount.length = offset;
        camera.mount.radians = actor.body.radians;
        camera.mount.apply();
    }

    public void render() {
        if (blackout != null) {
            blackout.render();
        }

        UtilsGraphics.drawPrepare();
        crosshair.render();
    }

    public void setActor(@Nullable Actor actor) {
        this.actor = actor;

        if (actor == null) {
            blackout = null;
            App.getCamera().mount.holder = null;
        } else {
            blackout = new Blackout(actor);
            App.getCamera().mount.holder = actor.body.position;
        }
    }

    @Nullable
    public Actor getActor() {
        return actor;
    }

    public float getRotation() {
        var aimingFactor = 1.0f;

        if (actor != null) {
            aimingFactor -= actor.isAiming.getCurrent() * MOUSE_SENSITIVITY_AIMING_FACTOR;
        }

        return Input.mouse.velocity.x * MOUSE_SENSITIVITY * aimingFactor;
    }

    public float getZooming() {
        var zooming = Input.mouse.wheel.getVelocitySmooth();

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_KP_ADD)) {
            zooming += ZOOM_VELOCITY;
        }

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_KP_SUBTRACT)) {
            zooming -= ZOOM_VELOCITY;
        }

        return Application.getCamera().scale
                * zooming
                * (float) Application.time.getDelta();
    }

}