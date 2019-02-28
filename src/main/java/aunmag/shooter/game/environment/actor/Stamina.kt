package aunmag.shooter.game.environment.actor

import aunmag.shooter.core.utilities.UtilsMath

class Stamina(private val actor: Actor) {

    var current = 1.0f
        private set(value) {
            field = UtilsMath.limitNumber(value, reserve, 1.0f)
        }
    private val reserve = 0.01f
    private val recurrenceStep = 0.07f

    fun update() {
        if (current < 1.0f) {
            current += recurrenceStep * actor.world.time.delta.toFloat() * actor.health
        }
    }

    fun spend(stepFactor: Float) {
        current -= recurrenceStep * stepFactor * actor.world.time.delta.toFloat()
    }

    /**
     * ~/src/python/stamina_efficiency.py
     */
    fun calculateEfficiency(): Float {
        return 1.0f - Math.pow(1.0 - current, 2.0).toFloat()
    }

}
