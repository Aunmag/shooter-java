package aunmag.shooter.game.data

import aunmag.shooter.game.client.App
import aunmag.shooter.game.environment.actor.Actor

val player: Actor?
    get() = App.main.game?.player?.actor
