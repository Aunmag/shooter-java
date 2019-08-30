package aunmag.shooter.game.ai.strategies;

import aunmag.shooter.core.utilities.Timer;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.ai.Ai;
import aunmag.shooter.game.ai.memory.Enemy;
import aunmag.shooter.game.environment.actor.Actor;
import aunmag.shooter.game.environment.actor.ActorType;

public abstract class Strategy {

    public static final int TIME_LIMIT = 30;

    public final Ai ai;
    public final float closeDistanceToDestination = UtilsMath.randomizeBetween(1, 2);
    public final float closeDistanceToEnemy = closeDistanceToDestination * 2;
    private final Timer timer;

    public Strategy(Ai ai) {
        this.ai = ai;
        this.timer = new Timer(ai.actor.world.time, TIME_LIMIT);
    }

    public void update() {
        analyze();
        proceed();
    }

    public void analyze() {}

    public void proceed() {}

    public boolean isExpired() {
        return timer.isDone();
    }

    /* Analyzing methods */

    public void findEnemy() {
        var actorOld = (Actor) null;
        var actorNew = (Actor) null;

        if (ai.enemy != null) {
            actorOld = ai.enemy.actor;
        }

        if (actorOld != null && (!actorOld.isAlive() || !actorOld.isActive())) {
            actorOld = null;
            ai.enemy = null;
        }

        for (var actor: ai.actor.world.actors.all) {
            if (actor.isAlive() && actor.type == ActorType.soldier) {
                actorNew = actor;
                break;
            }
        }

        if (actorNew != null && actorOld != actorNew) {
            ai.enemy = new Enemy(ai, actorNew);
        }
    }

    public boolean isClose(Enemy enemy) {
        return enemy.distanceSquared.get() < closeDistanceToEnemy * closeDistanceToEnemy;
    }

    public boolean isContact(Enemy enemy) {
        return Math.abs(enemy.angleRelative.get()) > UtilsMath.PIx0_5;
    }

    /* Proceeding methods */

    public void keepAttacking() {
        ai.actor.control.attack();
    }

    public void keepChasingEnemy() {
        var enemy = ai.enemy;

        if (enemy == null) {
            return;
        }

        ai.actor.control.turnTo(ai.enemy.direction.get());
        ai.actor.control.walkForward();

        if (isContact(enemy) && isClose(enemy)) {
            ai.actor.control.sprint();
        }
    }

}
