package aunmag.shooter.game.ai.strategies;

import aunmag.shooter.core.math.CollisionCC;
import aunmag.shooter.core.utilities.Timer;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.ai.Ai;
import aunmag.shooter.game.ai.memory.Destination;
import aunmag.shooter.game.ai.memory.Enemy;
import aunmag.shooter.game.environment.actor.Actor;
import aunmag.shooter.game.environment.actor.ActorType;
import org.joml.Vector2f;

public abstract class Strategy {

    public static final int TIME_LIMIT = 30;
    public static final int FIRE_DISTANCE = 10;
    public static final float AIMING_PRECISION = (float) Math.PI / 16;
    public static final float MAGAZINE_CAPACITY_RATIO_MIN = 0.8f;

    public final Ai ai;
    public final float dangerZoneRadius = UtilsMath.randomizeBetween(3, 6);
    private final Timer timer;

    public Strategy(Ai ai) {
        this.ai = ai;
        this.timer = new Timer(ai.actor.world.time, TIME_LIMIT);
    }

    public void update() {
        analyze();
        proceed();
    }

    public void analyze() {
        if (ai.enemy != null && !isEnemy(ai.enemy.actor)) {
            ai.enemy = null;
        }

        if (ai.reaction.isSlowPhase()) {
            ai.enemy = findEnemy();
        }
    }

    public void proceed() {
        var weapon = ai.actor.getWeapon();

        if (weapon != null && canReload()) {
            weapon.magazine.reload();
        }
    }

    public boolean isExpired() {
        return timer.isDone();
    }

    /* Analyzing methods */

    public Enemy findEnemy() {
        var me = ai.actor;
        var actorOld = (Actor) null;
        var actorNew = (Actor) null;

        if (ai.enemy != null) {
            actorOld = ai.enemy.actor;
        }

        var distance = Float.MAX_VALUE;

        for (var actor : me.world.actors.all) {
            if (isEnemy(actor)) {
                var distanceTest = me.body.position.distanceSquared(actor.body.position);

                if (distanceTest < distance) {
                    actorNew = actor;
                    distance = distanceTest;
                }
            }
        }

        if (actorNew == null || actorOld == actorNew) {
            return ai.enemy;
        } else {
            return new Enemy(ai, actorNew);
        }
    }

    public boolean isEnemy(Actor actor) {
        return (actor.type.genus == ActorType.Genus.HUMAN
                || ai.actor.type.genus == ActorType.Genus.HUMAN)
            && actor != ai.actor
            && actor.isActive()
            && actor.isAlive();
    }

    public boolean isDangerouslyClose(Enemy enemy) {
        return UtilsMath.closerThan(
                enemy.position,
                ai.actor.body.position,
                dangerZoneRadius
        );
    }

    public boolean isEyeContact(Enemy enemy) {
        return Math.abs(enemy.getRelativeAngle()) > UtilsMath.PIx0_5;
    }

    public boolean isInFireRange(Vector2f point) {
        return UtilsMath.closerThan(ai.actor.body.position, point, FIRE_DISTANCE);
    }

    public boolean isAimedAt(float direction) {
        return AIMING_PRECISION > Math.abs(UtilsMath.radiansDifference(
                ai.actor.body.radians,
                direction
        ));
    }

    public boolean isSafe() {
        // TODO: Also check if there's no enemy for some period of time
        return ai.enemy != null
            && ai.actor.hasWeapon()
            && !isInFireRange(ai.enemy.position);
    }

    public boolean canAttack(Enemy enemy) {
        if (ai.actor.hasWeapon()) {
            return isInFireRange(enemy.position);
        } else {
            return new CollisionCC(enemy.actor.body, ai.actor.hands.coverage).isTrue();
        }
    }

    public boolean canFire(Destination destination) {
        var weapon = ai.actor.getWeapon();

        return weapon != null
            && !weapon.trigger.isPressed()
            && !weapon.magazine.isReloading()
            && isAimedAt(destination.direction);
    }

    public boolean canReload() {
        var weapon = ai.actor.getWeapon();

        return weapon != null && (
            weapon.magazine.isEmpty()
            ||
            weapon.magazine.getVolumeRatio() < MAGAZINE_CAPACITY_RATIO_MIN && isSafe()
        );
    }

    /* Proceeding methods */

    public void attack(Enemy enemy) {
        ai.actor.control.turnTo(enemy.direction);

        if (ai.actor.hasWeapon()) {
            if (canFire(enemy)) {
                ai.actor.control.attack();
            }
        } else {
            ai.actor.control.walkForward();
            ai.actor.control.attack();
        }
    }

    public void chase(Enemy enemy) {
        ai.actor.control.turnTo(enemy.prediction.direction);
        ai.actor.control.walkForward();

        if (!ai.actor.hasWeapon() && isEyeContact(enemy) && isDangerouslyClose(enemy)) {
            ai.actor.control.sprint();
        }
    }

    public void keepAwayFrom(Enemy enemy) {
        ai.actor.control.turnTo(enemy.direction);
        ai.actor.control.walkBack();
    }

}
