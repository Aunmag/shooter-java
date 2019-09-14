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
    }

    @Override
    public void proceed() {
        if (ai.enemy != null) {
            if (mayAttack(ai.enemy.actor)) {
                keepAttacking();
            } else {
                keepChasingEnemy();
            }
        }
    }

}
