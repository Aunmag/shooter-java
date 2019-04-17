package aunmag.shooter.game.ai;

import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.UtilsGraphics;
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

            if (reaction.isQuickPhase() && enemy != null) {
                enemy.refresh();
            }

            if (strategy.isExpired()) {
                strategy = new ChaseStrategy(this);
            }

            strategy.update();
        }
    }

    public void render() {
        if (enemy != null) {
            var x = actor.body.position.x();
            var y = actor.body.position.y();

            GL11.glColor4f(1.0f, 0.4f, 0.4f, 0.25f);
            var position = enemy.position.get();
            var distance = strategy.closeDistanceToEnemy;
            UtilsGraphics.drawLine(x, y, position.x, position.y, true);
            UtilsGraphics.drawCircle(x, y, distance, false, true);
        }
    }

}
