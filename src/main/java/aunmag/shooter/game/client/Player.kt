package aunmag.shooter.game.client

import aunmag.shooter.core.Application
import aunmag.shooter.core.input.Input
import aunmag.shooter.game.environment.World
import aunmag.shooter.game.environment.actor.Actor
import aunmag.shooter.game.environment.weapon.Weapon
import aunmag.shooter.game.environment.weapon.WeaponType
import aunmag.shooter.game.ux.Blackout
import org.lwjgl.glfw.GLFW

class Player(actor: Actor) {

    val actor: Actor = actor
    val world: World = actor.getWorld()
    private var blackout = Blackout(actor)

    init {
        actor.weapon = Weapon(world, actor.type.primaryWeaponType)
        world.actors.all.add(actor)
        App.getCamera().mount.holder = actor.body.position
    }

    fun updateInput() {
        updateInputForActions()
        updateInputForRotation()
        updateInputForCamera()
    }

    private fun updateInputForActions() {
        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
            actor.control.walkForward()
        }

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
            actor.control.walkBack()
        }

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
            actor.control.walkLeft()
        }

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
            actor.control.walkRight()
        }

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            actor.control.sprint()
        }

        if (Input.mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
            actor.control.attack()
        }

        if (Input.mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_2)) {
            actor.control.aim()
        }

        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_R) && actor.hasWeapon) {
            actor.weapon.magazine.reload()
        }
    }

    private fun updateInputForRotation() {
        val mouseSensitivity = 0.003f * (1f - actor.isAiming.current * 0.75f)
        actor.body.radians += Input.mouse.velocity.x * mouseSensitivity
        actor.body.correctRadians()
    }

    private fun updateInputForCamera() {
        var zoom = Input.mouse.wheel.velocitySmooth

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_KP_ADD)) {
            zoom += 1.0f
        }

        if (Input.keyboard.isKeyDown(GLFW.GLFW_KEY_KP_SUBTRACT)) {
            zoom -= 1.0f
        }

        if (zoom != 0.0f) {
            val camera = Application.getCamera()
            camera.scaleZoom += zoom * camera.scaleZoom * Application.time.delta.toFloat()
        }
    }

    fun updateCameraPosition() {
        val camera = App.getCamera()
        val window = App.getWindow()
        val offsetMin = window.centerY / 2.0f / camera.scaleFull
        val offset = offsetMin * (1.0f + actor.isAiming.current)

        camera.radians = actor.body.radians
        camera.mount.length = offset
        camera.mount.radians = actor.body.radians
        camera.mount.apply()
    }

    fun renderUx() {
        blackout.render()
    }

}
