package aunmag.shooter.game.scenarios

import aunmag.shooter.core.utilities.Operative
import aunmag.shooter.game.environment.World
import aunmag.shooter.game.environment.actor.Actor

abstract class Scenario(val world: World) : Operative() {

    abstract fun createPlayableActor() : Actor?

}
