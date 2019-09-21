package aunmag.shooter.game.ai.strategies;

import aunmag.shooter.game.ai.Ai;

public class ChaseStrategy extends Strategy {

    public ChaseStrategy(Ai ai) {
        super(ai);
    }

    @Override
    public void proceed() {
        super.proceed();

        var enemy = ai.enemy;
        if (enemy == null) {
            return;
        }

        if (canAttack(enemy)) {
            attack(enemy);
        } else {
            chase(enemy);
        }
    }

}
