package aunmag.shooter.game.client.player;

import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.client.Context;
import aunmag.shooter.game.environment.actor.Actor;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class Player extends Operative {

    public static final float MOUSE_SENSITIVITY = 0.003f;
    public static final float MOUSE_SENSITIVITY_AIMING_FACTOR = 0.75f;
    public static final float ZOOM_VELOCITY = 1.5f;
    public static final float ZOOM_VELOCITY_SCROLL = 10f;
    public static final float ZOOM_AVERAGING_TIME = 0.25f;
    public static final float SCALE_MIN = 5.0f;
    public static final float SCALE_MAX = 30.0f;
    public static final float SCALE_DEFAULT = 15.0f;
    public static final float CAMERA_OFFSET_RATIO = 0.25f;

    public final Hud hud = new Hud();
    @Nullable private Actor actor = null;
    @Nullable private Blackout blackout = null;
    @Nullable public CameraShaker cameraShaker = null;
    private final Crosshair crosshair = new Crosshair();
    private float direction = 0;
    private float zooming = 0;
    private boolean isAiming = false;

    public Player() {
        Context.main.getCamera().scale = SCALE_DEFAULT;
    }

    public void updateInput() {
        if (actor == null) {
            return;
        }

        var input = Context.main.getInput();

        if (input.keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
            actor.control.walkForward();
        }

        if (input.keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
            actor.control.walkBack();
        }

        if (input.keyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
            actor.control.walkLeft();
        }

        if (input.keyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
            actor.control.walkRight();
        }

        if (input.keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            actor.control.sprint();
        }

        if (input.mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
            actor.control.attack();
        }

        if (input.mouse.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_2)) {
            isAiming = !isAiming;
        }

        if (isAiming) {
            actor.control.aim();
        }

        if (input.keyboard.isKeyPressed(GLFW.GLFW_KEY_R) && actor.hasWeapon()) {
            actor.getWeapon().magazine.reload();
        }

        updateRotation();
        updateZooming();
    }

    private void updateRotation() {
        if (actor == null) {
            return;
        }

        direction += MOUSE_SENSITIVITY
            * Context.main.getInput().mouse.velocity.x
            * (1 - actor.isAiming.getCurrent() * MOUSE_SENSITIVITY_AIMING_FACTOR);

        actor.control.turnTo(direction);
    }

    private void updateZooming() {
        var camera = Context.main.getCamera();
        var input = Context.main.getInput();
        var zoomingNew = input.mouse.scroll * ZOOM_VELOCITY_SCROLL;

        if (input.keyboard.isKeyDown(GLFW.GLFW_KEY_KP_ADD)) {
            zoomingNew += ZOOM_VELOCITY;
        }

        if (input.keyboard.isKeyDown(GLFW.GLFW_KEY_KP_SUBTRACT)) {
            zoomingNew -= ZOOM_VELOCITY;
        }

        zooming = UtilsMath.average(
            zooming,
            Context.main.application.frameRate.getFrequency() * ZOOM_AVERAGING_TIME,
            zoomingNew
        );

        camera.scale = UtilsMath.limit(
            camera.scale - (camera.scale * zooming * Context.main.getDelta()),
            SCALE_MIN,
            SCALE_MAX
        );
    }

    @Override
    public void update() {
        hud.update();
        updateCameraPosition();

        if (cameraShaker != null) {
            cameraShaker.update();
        }
    }

    private void updateCameraPosition() {
        if (actor == null) {
            return;
        }

        var camera = Context.main.getCamera();
        var window = Context.main.getWindow();
        var offset = camera.toMeters(
                window.getHeight()
                * CAMERA_OFFSET_RATIO
                * (1.0f + actor.isAiming.getCurrent())
        );

        camera.setRadians(direction);
        camera.mount.length = offset;
        camera.mount.radians = direction;
        camera.mount.apply();
    }

    @Override
    public void render() {
        if (blackout != null) {
            blackout.render();
        }

        crosshair.render();
        hud.render();
    }

    @Override
    protected void onRemove() {
        hud.remove();
        super.onRemove();
    }

    public void setActor(@Nullable Actor actor) {
        this.actor = actor;

        if (actor == null) {
            blackout = null;
            cameraShaker = null;
            Context.main.getCamera().mount.holder = null;
        } else {
            blackout = new Blackout(actor);
            cameraShaker = new CameraShaker(actor.world.time);
            direction = actor.getDirectionDesired();
            Context.main.getCamera().mount.holder = actor.body.position;
        }
    }

    @Nullable
    public Actor getActor() {
        return actor;
    }

}
