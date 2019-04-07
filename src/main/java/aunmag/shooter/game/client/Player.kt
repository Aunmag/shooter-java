package aunmag.shooter.game.client

import aunmag.shooter.core.Application
import aunmag.shooter.core.input.Input
import aunmag.shooter.core.utilities.UtilsMath
import aunmag.shooter.game.environment.World
import aunmag.shooter.game.environment.actor.Actor
import aunmag.shooter.game.environment.actor.ActorType
import aunmag.shooter.game.environment.weapon.Weapon
import aunmag.shooter.game.environment.weapon.WeaponType
import aunmag.shooter.game.ux.Blackout
import org.lwjgl.glfw.GLFW

class Player(world: World) {

    // Текущий игрок
    var actor = Actor(ActorType.human, world, 0f, 0f,
            -UtilsMath.PIx0_5.toFloat())
    private var blackout = Blackout(actor)

    init {
        actor.weapon = Weapon(world, WeaponType.pm)
        world.actors.all.add(actor)
        App.getCamera().mount.holder = actor.body.position
    }

    fun updateInput() {
        updateInputForActions()
        updateInputForRotation()
        updateInputForCamera()
    }

    private fun updateInputForActions() {
        actor.isWalkingForward = Input.keyboard.isKeyDown(GLFW.GLFW_KEY_W)
        actor.isWalkingBack = Input.keyboard.isKeyDown(GLFW.GLFW_KEY_S)
        actor.isWalkingLeft = Input.keyboard.isKeyDown(GLFW.GLFW_KEY_A)
        actor.isWalkingRight = Input.keyboard.isKeyDown(GLFW.GLFW_KEY_D)
        actor.isSprinting = Input.keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)
        actor.isAttacking = Input.mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)

        if (Input.mouse.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_2)) {
            actor.isAiming.toggle()
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
