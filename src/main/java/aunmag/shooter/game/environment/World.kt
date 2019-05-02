package aunmag.shooter.game.environment

import aunmag.shooter.core.Application
import aunmag.shooter.core.gui.Layer
import aunmag.shooter.core.utilities.OperativeManager
import aunmag.shooter.core.utilities.TimeFlow
import aunmag.shooter.core.utilities.UtilsGraphics
import aunmag.shooter.game.ai.Ai
import aunmag.shooter.game.client.Context
import aunmag.shooter.game.data.soundAmbiance
import aunmag.shooter.game.data.soundAtmosphere
import aunmag.shooter.game.environment.actor.Actor
import aunmag.shooter.game.environment.decorations.Decoration
import aunmag.shooter.game.environment.projectile.Projectile
import aunmag.shooter.game.environment.terrain.Terrain
import aunmag.shooter.game.environment.utils.TreesGenerator
import aunmag.shooter.game.items.ItemWeapon
import org.lwjgl.opengl.GL11

class World {

    val time = TimeFlow()
    private val terrain = Terrain()
    val ground = OperativeManager<Decoration>()
    val ais = OperativeManager<Ai>()
    val actors = OperativeManager<Actor>()
    val projectiles = OperativeManager<Projectile>()
    val notifications = Layer()
    val itemsWeapon = OperativeManager<ItemWeapon>()
    val trees = OperativeManager<Decoration>()

    init {
        TreesGenerator(this, 96f, 1f / 48f, 3.5f).generate()
    }

    fun update() {
        time.add(Application.time.delta, true)
        ais.update()
        actors.update()
        projectiles.update()
        itemsWeapon.update()
        notifications.update()
    }

    // TODO: Optimize draw modes
    fun render() {
        terrain.render()

        if (!Context.main.isDebug) {
            Application.getShader().bind()
            ground.render()
        }

        itemsWeapon.render()

        if (Context.main.isDebug) {
            UtilsGraphics.drawPrepare()
        }

        actors.render()

        if (!Context.main.isDebug) {
            UtilsGraphics.drawPrepare()
        }

        projectiles.render()
        GL11.glLineWidth(1f)

        if (Context.main.isDebug) {
            ais.render()
        }

        if (!Context.main.isDebug) {
            Application.getShader().bind()
            trees.render()
        }

        notifications.render()
    }

    fun playSounds() {
        soundAmbiance.play()
        soundAtmosphere.play()
    }

    fun stopSounds() {
        soundAmbiance.stop()
        soundAtmosphere.stop()
    }

    fun remove() {
        notifications.remove()
        ground.remove()
        trees.remove()
        ais.remove()
        actors.remove()
        projectiles.remove()
        itemsWeapon.remove()
        stopSounds()
    }

}
