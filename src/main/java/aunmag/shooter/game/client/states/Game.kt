package aunmag.shooter.game.client.states

import aunmag.shooter.core.input.Input
import aunmag.shooter.core.utilities.UtilsGraphics
import aunmag.shooter.game.client.App
import aunmag.shooter.game.client.Player
import aunmag.shooter.game.environment.actor.Actor
import aunmag.shooter.game.client.graphics.CameraShaker
import aunmag.shooter.game.client.graphics.Crosshair
import aunmag.shooter.game.environment.World
import aunmag.shooter.game.client.states.ScenarioStatus;
import aunmag.shooter.game.scenarios.ScenarioEncircling
import aunmag.shooter.game.scenarios.Scenario
import aunmag.shooter.game.ux.Hud
import org.lwjgl.glfw.GLFW

class Game {

    val world: World = World()
    val player: Player = Player(world)
    val crosshair: Crosshair = Crosshair(player.actor)
    private val scenario: Scenario = ScenarioEncircling(world)
    private val hud: Hud = Hud()
    
    init {
        // When creating an instance, change its player type to the selected one
        player.actor.setType(ScenarioStatus.scenarioEncircling.actorType);
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
            App.main.isPause = true
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
