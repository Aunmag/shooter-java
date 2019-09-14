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
        this.reaction = new Reaction(actor.world.time, actor.type.reaction);
    }

    @Override
    public void update() {
        reaction.update();

        if (reaction.isQuickPhase() && enemy != null) {
            enemy = new Enemy(enemy);
        }

        if (strategy.isExpired()) {
            strategy = new ChaseStrategy(this);
        }

        strategy.update();
    }

    @Override
    public void render() {
        if (enemy != null) {
            enemy.render();
        }
    }

    @Override
    public boolean isActive() {
        return super.isActive() && actor.isActive() && actor.isAlive();
    }

    public Strategy getStrategy() {
        return strategy;
    }

}
