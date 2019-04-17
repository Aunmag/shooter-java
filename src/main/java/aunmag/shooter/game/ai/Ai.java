package aunmag.shooter.game.ai;

import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.game.ai.memory.Enemy;
import aunmag.shooter.game.ai.strategies.ChaseStrategy;
import aunmag.shooter.game.ai.strategies.Strategy;
import aunmag.shooter.game.environment.actor.Actor;
import org.jetbrains.annotations.Nullable;

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
            enemy.render();
        }
    }

    /* Getters */

    public Strategy getStrategy() {
        return strategy;
    }

}
