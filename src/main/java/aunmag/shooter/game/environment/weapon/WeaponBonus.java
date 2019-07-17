package aunmag.shooter.game.environment.weapon;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.gui.font.FontStyle;
import aunmag.shooter.core.gui.font.Text;
import aunmag.shooter.core.input.Input;
import aunmag.shooter.core.math.BodyCircle;
import aunmag.shooter.core.math.CollisionCC;
import aunmag.shooter.core.utilities.FluidValue;
import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.Timer;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.client.Context;
import aunmag.shooter.game.environment.actor.Actor;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class WeaponBonus extends Operative {

    private static final float LIFETIME = 30;
    private static final float ROTATION_VELOCITY = (float) Math.PI;
    private static final float PULSE_TIME = 0.4f;
    private static final float RADIUS_MIN = 0.12f;
    private static final float RADIUS_MAX = 0.18f;

    public final BodyCircle body;
    public final Weapon weapon;
    @Nullable
    private Actor giver;
    private final Text text;
    private final FluidValue pulse;
    private final Timer timer;

    public WeaponBonus(float x, float y, Weapon weapon, @Nullable Actor giver) {
        this.weapon = weapon;
        this.giver = giver;

        body = new BodyCircle(x, y, 0, 0);
        text = new Text(x, y, weapon.type.name, FontStyle.SIMPLE);
        text.setOnWorldRendering(true);

        pulse = new FluidValue(weapon.world.time, PULSE_TIME);
        timer = new Timer(weapon.world.time, LIFETIME);
        timer.next();
    }

    public WeaponBonus(float x, float y, Weapon weapon) {
        this(x, y, weapon, null);
    }

    public WeaponBonus(Actor giver, Weapon weapon) {
        this(
            giver.body.position.x,
            giver.body.position.y,
            weapon,
            giver
        );
    }

    private void drop() {
        if (giver != null) {
            body.position.x = giver.body.position.x;
            body.position.y = giver.body.position.y;
            text.getPosition().x = giver.body.position.x;
            text.getPosition().y = giver.body.position.y;
        }

        timer.next();
        giver = null;
    }

    @Override
    public void update() {
        if (giver == null) {
            updateColor();
            updateRadius();
            updateWeapon();
            updatePickup();
        } else if (!giver.isAlive() || !giver.isActive()) {
            drop();
        }
    }

    private void updateColor() {
        var alpha = UtilsMath.limit(
                4.0f * (float) (1 - timer.calculateIsDoneRatio()),
                0.0f,
                0.8f
        );

        body.color.set(0.9f, 0.9f, 0.9f, alpha / 2);
        text.setColour(new Vector4f(1, 1, 1, alpha));
    }

    private void updateRadius() {
        pulse.update();

        if (pulse.isTargetReached()) {
            if (pulse.getTarget() == RADIUS_MIN) {
                pulse.setTarget(RADIUS_MAX);
            } else {
                pulse.setTarget(RADIUS_MIN);
            }
        }

        body.radius = pulse.getCurrent();
    }

    private void updateWeapon() {
        weapon.body.positionTail.set(body.position);
        weapon.body.radians -= ROTATION_VELOCITY * weapon.world.time.getDelta();
        weapon.update();
    }

    private void updatePickup() {
        var actor = Context.main.getPlayerActor();

        if (actor == null) {
            return;
        }

        var collision = new CollisionCC(body, actor.hands.coverage);

        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_E) && collision.isTrue()) {
            var previousWeapon = actor.getWeapon();

            if (previousWeapon != null) {
                actor.world.bonuses.all.add(new WeaponBonus(
                        body.position.x,
                        body.position.y,
                        previousWeapon
                ));
            }

            actor.setWeapon(weapon);
            remove();
        }
    }

    @Override
    public void render() {
        if (giver == null) {
            body.render();
            text.orderRendering();

            if (!Context.main.isDebug()) {
                Application.getShader().bind();
                weapon.render();
            }
        }
    }

    @Override
    protected void onRemove() {
        text.remove();
        super.onRemove();
    }

    @Override
    public boolean isActive() {
        return super.isActive() && (giver != null || !timer.isDone());
    }

}
