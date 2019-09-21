package aunmag.shooter.game.ai.strategies;

import aunmag.shooter.core.math.CollisionCC;
import aunmag.shooter.core.utilities.Timer;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.ai.Ai;
import aunmag.shooter.game.ai.memory.Enemy;
import aunmag.shooter.game.environment.actor.Actor;
import aunmag.shooter.game.environment.actor.ActorType;

public abstract class Strategy {

    public static final int TIME_LIMIT = 30;

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

    public void proceed() {}

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
        return (actor.type == ActorType.human || ai.actor.type == ActorType.human)
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

    public boolean canAttack(Enemy enemy) {
        return new CollisionCC(enemy.actor.body, ai.actor.hands.coverage).isTrue();
    }

    /* Proceeding methods */

    public void attack(Enemy enemy) {
        ai.actor.control.turnTo(enemy.direction);
        ai.actor.control.walkForward();
        ai.actor.control.attack();
    }

    public void chase(Enemy enemy) {
        ai.actor.control.turnTo(enemy.prediction.direction);
        ai.actor.control.walkForward();

        if (isEyeContact(enemy) && isDangerouslyClose(enemy)) {
            ai.actor.control.sprint();
        }
    }

}
