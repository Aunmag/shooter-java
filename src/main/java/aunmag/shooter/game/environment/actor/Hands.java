package aunmag.shooter.game.environment.actor;

import aunmag.shooter.core.math.BodyCircle;
import aunmag.shooter.core.math.CollisionCC;
import aunmag.shooter.core.utilities.Timer;

public class Hands {

    public static final float DISTANCE = 0.34375f;
    public static final float COVERAGE_RADIUS = 0.34f;
    public static final float RELOADING_TIME = 0.4f;
    public static final float RELOADING_TIME_DEVIATION_FACTOR = 0.125f;
    public static final float SHAKE_FACTOR = 85;

    public final Actor actor;
    public final Timer attackTimer;
    public final BodyCircle coverage;

    public Hands(Actor actor) {
        this.actor = actor;
        coverage = new BodyCircle(0, 0, 0, COVERAGE_RADIUS);
        coverage.color.set(1f, 0f, 0f, 0.5f);

        attackTimer = new Timer(
                actor.world.time,
                RELOADING_TIME,
                RELOADING_TIME_DEVIATION_FACTOR
        );
        updatePosition();
    }

    public void update() {
        updatePosition();

        if (
                actor.control.isAttacking()
                && !actor.getHasWeapon()
                && attackTimer.isDone()
        ) {
            attack();
            attackTimer.next();
        }
    }

    public void updatePosition() {
        float radians = actor.body.radians;
        float x = actor.body.position.x + COVERAGE_RADIUS * (float) Math.cos(radians);
        float y = actor.body.position.y + COVERAGE_RADIUS * (float) Math.sin(radians);
        coverage.position.set(x, y);
    }

    private void attack() {
        for (Actor opponent: actor.world.actors.all) {
            if (actor.type == opponent.type || opponent == actor) {
                continue;
            }

            if (new CollisionCC(coverage, opponent.body).isTrue()) {
                var force = actor.type.damage * actor.getHealth();
                opponent.hit(force, actor);
                opponent.shake(force * SHAKE_FACTOR, true);
            }
        }
    }

}
