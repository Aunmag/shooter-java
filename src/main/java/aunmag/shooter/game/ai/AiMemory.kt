package aunmag.shooter.game.ai

abstract class AiMemory {

    abstract fun forget()
    abstract fun isInMemory(): Boolean

}
