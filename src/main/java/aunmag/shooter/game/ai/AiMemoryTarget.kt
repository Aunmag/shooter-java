package aunmag.shooter.game.ai

import aunmag.shooter.game.environment.actor.Actor

class AiMemoryTarget : AiMemory() {

    var actor: Actor? = null
    var distance = 0f
    var direction = 0f
    var radiansDifference = 0f
    var isReached = false

    init {
        forget()
    }

    override fun forget() {
        actor = null
        isReached = false
    }

    override fun isInMemory() : Boolean {
        return actor != null
    }

}
