package aunmag.shooter.game.ai.strategies;

import aunmag.shooter.game.ai.Ai;

public class ChaseStrategy extends Strategy {

    public ChaseStrategy(Ai ai) {
        super(ai);
    }

    @Override
    public void analyze() {
        if (ai.reaction.isSlowPhase()) {
            findEnemy();
        }

        var enemy = ai.enemy;
        var bypass = ai.bypass;

        if (bypass == null || isClose(bypass) || enemy == null || isClose(enemy)) {
            ai.bypass = null;
        }
    }

    @Override
    public void proceed() {
        if (ai.enemy != null) {
            if (ai.enemy.isReached.get()) {
                keepAttacking();
            } else {
                keepChasingEnemy();
            }
        }
    }

}
