package aunmag.shooter.game.client.states

import aunmag.shooter.core.input.Input
import aunmag.shooter.game.client.Context
import aunmag.shooter.game.client.Hud
import aunmag.shooter.game.client.player.CameraShaker
import aunmag.shooter.game.client.player.Player
import aunmag.shooter.game.environment.World
import aunmag.shooter.game.scenarios.ScenarioEncircling
import org.lwjgl.glfw.GLFW

class Game {

    val world = World()
    val player = Player()
    private val scenario = ScenarioEncircling(world)
    private val hud = Hud()

    init {
        player.actor = scenario.createPlayableActor()
    }

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
        player.render()
        scenario.render()
        hud.render()
    }

    fun remove() {
        world.remove()
        scenario.remove()
        hud.remove()
    }

}
