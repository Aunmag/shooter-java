package aunmag.shooter.game.client.states

import aunmag.shooter.core.input.Input
import aunmag.shooter.core.utilities.UtilsGraphics
import aunmag.shooter.game.client.Context
import aunmag.shooter.game.client.Player
import aunmag.shooter.game.client.graphics.CameraShaker
import aunmag.shooter.game.client.graphics.Crosshair
import aunmag.shooter.game.environment.World
import aunmag.shooter.game.scenarios.ScenarioEncircling
import aunmag.shooter.game.ux.Hud
import org.lwjgl.glfw.GLFW

class Game {

    val world = World()
    val player = Player(world)
    private val scenario = ScenarioEncircling(world)
    private val crosshair = Crosshair(player.actor) // TODO: Change implementation
    private val hud = Hud()

    fun resume() {
        world.playSounds()
    }

    fun suspend() {
        world.stopSounds()
    }

    fun update() {
        player.updateInput()
        world.update()
        scenario.update()
        player.updateCameraPosition()
        CameraShaker.update()

        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            Context.main.application.isPause = true
        }

        hud.update()
    }

    fun render() {
        world.render()
        player.renderUx()
        UtilsGraphics.drawPrepare()
        crosshair.render()
        scenario.render()
        hud.render()
    }

    fun remove() {
        world.remove()
        scenario.remove()
        hud.remove()
    }

}
