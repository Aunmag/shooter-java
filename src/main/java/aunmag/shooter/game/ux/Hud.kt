package aunmag.shooter.game.ux

import aunmag.shooter.core.gui.font.FontStyle
import aunmag.shooter.core.gui.font.Text
import aunmag.shooter.core.utilities.UtilsMath
import aunmag.shooter.game.client.App
import aunmag.shooter.game.data.player
import aunmag.shooter.game.gui.Parameter

class Hud {

    private val health = Parameter("Health", 0.0f, 30, 32)
    private val stamina = Parameter("Stamina", 0.0f, 30, 33)
    private val ammo = Parameter("Ammo", 0.0f, 30, 34)
    private val debug = Text(10f, 10f, "", FontStyle.SIMPLE)

    fun update() {
        Parameter.update()
        health.value = player?.health ?: 0f
        stamina.value = player?.stamina?.current ?: 0f
        ammo.value = player?.weapon?.magazine?.calculateVolumeRatio() ?: 0f
        ammo.isPulsing = player?.weapon?.magazine?.isReloading ?: false
    }

    fun render() {
        health.render()
        stamina.render()
        ammo.render()

        if (App.main.isDebug) {
            renderDebug()
        }
    }

    private fun renderDebug() {
        val game = App.main.game ?: return
        val world = game.world

        var timeSpentUpdate = 0f // TODO: Invoke data
        var timeSpentRender = 0f // TODO: Invoke data
        var timeSpentTotal = timeSpentUpdate + timeSpentRender
        val round = 100f
        timeSpentUpdate = UtilsMath.calculateRoundValue(timeSpentUpdate, round)
        timeSpentRender = UtilsMath.calculateRoundValue(timeSpentRender, round)
        timeSpentTotal = UtilsMath.calculateRoundValue(timeSpentTotal, round)

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
        health.remove()
        stamina.remove()
        ammo.remove()
        debug.remove()
    }

}
