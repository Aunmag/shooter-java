package aunmag.shooter.game.items

import aunmag.shooter.core.Application
import aunmag.shooter.core.gui.font.FontStyle
import aunmag.shooter.core.gui.font.Text
import aunmag.shooter.core.input.Input
import aunmag.shooter.core.math.BodyCircle
import aunmag.shooter.core.math.CollisionCC
import aunmag.shooter.core.utilities.FluidValue
import aunmag.shooter.core.utilities.Operative
import aunmag.shooter.core.utilities.Timer
import aunmag.shooter.core.utilities.UtilsMath
import aunmag.shooter.game.client.Context
import aunmag.shooter.game.environment.actor.Actor
import aunmag.shooter.game.environment.weapon.Weapon
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW

class ItemWeapon private constructor(
        x: Float,
        y: Float,
        val weapon: Weapon,
        private var giver: Actor? = null
) : Operative() {

    constructor(x: Float, y: Float, weapon: Weapon) : this(x, y, weapon, null)
    constructor(giver: Actor, weapon: Weapon) : this(
            giver.body.position.x,
            giver.body.position.y,
            weapon,
            giver
    )

    val body = BodyCircle(x, y, 0f, 0f)
    private val timer = Timer(weapon.world.time, 15.0)
    private val pulse = FluidValue(weapon.world.time, 0.4)
    private val pulseMin = 0.12f
    private val pulseMax = 0.18f
    private val rotationVelocity = Math.PI.toFloat()
    private val text = Text(x, y, weapon.type.name, FontStyle.SIMPLE)

    init {
        text.setOnWorldRendering(true)
        timer.next()
    }

    private fun drop() {
        giver?.let {
            body.position.x = it.body.position.x
            body.position.y = it.body.position.y
            text.position.x = it.body.position.x
            text.position.y = it.body.position.y
        }

        timer.next()
        giver = null
    }

    override fun update() {
        val owner = this.giver
        if (owner == null) {
            if (timer.isDone) {
                remove()
            } else {
                updateColor()
                updateRadius()
                updateWeapon()
                updatePickup()
            }
        } else if (!owner.isAlive || owner.isRemoved) {
            drop()
        }
    }

    private fun updateColor() {
        val alpha = UtilsMath.limitNumber(
                4.0f * (1.0 - timer.calculateIsDoneRatio()).toFloat(),
                0.0f,
                0.8f
        )

        body.color.set(0.9f, 0.9f, 0.9f, alpha / 2f)
        text.setColour(Vector4f(1f, 1f, 1f, alpha))
    }

    private fun updateRadius() {
        pulse.update()

        if (pulse.isTargetReached) {
            pulse.target = if (pulse.target == pulseMin) {
                pulseMax
            } else {
                pulseMin
            }
        }

        body.radius = pulse.current
    }

    private fun updateWeapon() {
        weapon.body.positionTail.set(body.position)
        weapon.body.radians -= rotationVelocity * weapon.world.time.delta.toFloat()
        weapon.update()
    }

    private fun updatePickup() {
        val player: Actor = Context.main.playerActor ?: return
        val collision = CollisionCC(body, player.hands.coverage)

        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_E) && collision.isTrue) {
            player.weapon?.let {
                val replacement = ItemWeapon(body.position.x, body.position.y, it)
                player.world.itemsWeapon.all.add(replacement)
            }

            player.weapon = weapon
            remove()
        }
    }

    override fun render() {
        if (giver == null) {
            body.render()
            text.orderRendering()

            if (!Context.main.isDebug) {
                Application.getShader().bind()
                weapon.render()
            }
        }
    }

    override fun remove() {
        if (isRemoved) {
            return
        }

        text.remove()
        super.remove()
    }

}
