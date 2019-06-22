package aunmag.shooter.game.client

import aunmag.shooter.core.gui.Layer
import aunmag.shooter.core.gui.Parameter
import aunmag.shooter.core.gui.font.FontStyle
import aunmag.shooter.core.gui.font.Text
import aunmag.shooter.core.utilities.UtilsMath

class Hud {

    private val layer = Layer()
    private val health = Parameter("Health", 0.0f, 30, 32)
    private val stamina = Parameter("Stamina", 0.0f, 30, 33)
    private val ammo = Parameter("Ammo", 0.0f, 30, 34)
    private val debug = Text(10f, 10f, "", FontStyle.SIMPLE)

    init {
        layer.add(health)
        layer.add(stamina)
        layer.add(ammo)
    }

    fun update() {
        health.value = Context.main.playerActor?.health ?: 0f
        stamina.value = Context.main.playerActor?.stamina?.current ?: 0f
        ammo.value = Context.main.playerActor?.weapon?.magazine?.calculateVolumeRatio() ?: 0f
        ammo.isPulsing = Context.main.playerActor?.weapon?.magazine?.isReloading ?: false
    }

    fun render() {
        layer.render()

        if (Context.main.application.isDebug) {
            renderDebug()
        }
    }

    private fun renderDebug() {
        val game = Context.main.game ?: return
        val world = game.world

        var timeSpentUpdate = 0f // TODO: Invoke data
        var timeSpentRender = 0f // TODO: Invoke data
        var timeSpentTotal = timeSpentUpdate + timeSpentRender
        val round = 100f
        timeSpentUpdate = UtilsMath.round(timeSpentUpdate, round)
        timeSpentRender = UtilsMath.round(timeSpentRender, round)
        timeSpentTotal = UtilsMath.round(timeSpentTotal, round)

        var message = ""
        message += String.format("Spent time on updating: %s ms\n", timeSpentUpdate)
        message += String.format("Spent time on rendering: %s ms\n", timeSpentRender)
        message += String.format("Spent time total: %s ms \n", timeSpentTotal)
        message += String.format("\nAIs: %s", world.ais.all.size)
        message += String.format("\nActors: %s", world.actors.all.size)
        message += String.format("\nBullets: %s", world.projectiles.all.size)
        message += String.format("\nGround: %s", world.ground.all.size)
        message += String.format("\nTrees: %s", world.trees.all.size)

        debug.load(message)
        debug.orderRendering()
    }

    fun remove() {
        layer.remove()
        debug.remove()
    }

}
