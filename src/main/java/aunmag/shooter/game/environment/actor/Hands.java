package aunmag.shooter.game.environment.actor;

import aunmag.shooter.core.math.BodyCircle;
import aunmag.shooter.core.math.CollisionCC;
import aunmag.shooter.core.utilities.Timer;
import aunmag.shooter.game.Config;

public class Hands {

    @Config public static final float DISTANCE = 0.34375f;
    @Config public static final float COVERAGE_RADIUS = 0.34f;
    @Config public static final float RELOADING_TIME = 0.4f;
    @Config public static final float RELOADING_TIME_DEVIATION_FACTOR = 0.125f;

    public final Actor actor;
    public final Timer attackTimer;
    public final BodyCircle coverage;

    public Hands(Actor actor) {
        this.actor = actor;
        coverage = new BodyCircle(0, 0, 0, COVERAGE_RADIUS);
        coverage.color.set(1f, 0f, 0f, 0.5f);

        attackTimer = new Timer(
                actor.world.getTime(),
                RELOADING_TIME,
                RELOADING_TIME_DEVIATION_FACTOR
        );
        updatePosition();
    }

    public void update() {
        updatePosition();

        if (actor.isAttacking && !actor.getHasWeapon() && attackTimer.isDone()) {
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
        for (Actor opponent: actor.world.getActors().all) {
            if (actor.type == opponent.type || opponent == actor) {
                continue;
            }

            if (new CollisionCC(coverage, opponent.body).isTrue()) {
                opponent.hit(actor.type.damage * actor.getHealth(), actor);
            }
        }
    }

}
