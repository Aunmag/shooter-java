package aunmag.shooter.game.ai;

import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.UtilsGraphics;
import aunmag.shooter.game.ai.memory.Bypass;
import aunmag.shooter.game.ai.memory.Enemy;
import aunmag.shooter.game.ai.strategies.ChaseStrategy;
import aunmag.shooter.game.ai.strategies.Strategy;
import aunmag.shooter.game.environment.actor.Actor;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class Ai extends Operative {

    public final Actor actor;
    public final Reaction reaction;
    private Strategy strategy;
    @Nullable public Enemy enemy = null;
    @Nullable public Bypass bypass = null;

    public Ai(Actor actor) {
        this.actor = actor;
        this.strategy = new ChaseStrategy(this);
        this.reaction = new Reaction(actor.world.getTime(), actor.type.reaction);
    }

    public void update() {
        if (actor.isRemoved() || !actor.isAlive()) {
            remove();
        } else {
            reaction.update();
            strategy.update();

            if (reaction.isFirstPhase()) {
                if (enemy != null) {
                    enemy.refresh();
                }

                if (bypass != null) {
                    bypass.refresh();
                }
            }

            if (strategy.isExpired()) {
                strategy = new ChaseStrategy(this);
            }

            reaction.next();
        }
    }

    public void render() {
        var x = actor.body.position.x();
        var y = actor.body.position.y();
        var min = 0.4f;
        var max = 1.0f;
        var alpha = 0.25f;

        if (bypass != null) {
            GL11.glColor4f(max, max, min, alpha);
            var position = bypass.position.get();
            var distance = strategy.closeDistanceToDestination;
            UtilsGraphics.drawLine(x, y, position.x, position.y, true);
            UtilsGraphics.drawCircle(x, y, distance, false, true);
        }

        if (enemy != null) {
            GL11.glColor4f(max, min, min, alpha);
            var position = enemy.position.get();
            var distance = strategy.closeDistanceToEnemy;
            UtilsGraphics.drawLine(x, y, position.x, position.y, true);
            UtilsGraphics.drawCircle(x, y, distance, false, true);
        }
    }

}
