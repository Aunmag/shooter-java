package aunmag.shooter.game.gui

import aunmag.shooter.core.Application
import aunmag.shooter.core.gui.Grid
import aunmag.shooter.core.gui.Label
import aunmag.shooter.core.gui.font.FontStyle
import aunmag.shooter.core.utilities.FluidToggle
import aunmag.shooter.core.utilities.UtilsGraphics
import org.joml.Vector4f
import org.lwjgl.opengl.GL11

class Parameter(title: String, var value: Float, x: Int, y: Int) {

    companion object {
        private val grid = Grid(36)
        private val pulse = FluidToggle(Application.time, 0.2)

        fun update() {
            pulse.update()
            if (pulse.isTargetReached) {
                pulse.toggle()
            }
        }
    }

    var isPulsing = false
    private val label = Label(grid, x, y, 1, 1, title, FontStyle.LABEL_LIGHT)

    init {
        label.setTextColour(Vector4f(1.0f, 1.0f, 1.0f, 0.5f))
    }

    fun render() {
        val width = grid.stepX * 3.0f
        val height = grid.stepY / 2.0f
        val x = grid.stepX * (label.quad.position.x + 2.0f)
        val y = grid.stepY * (label.quad.position.y + 0.3f)
        val a = width * value
        val b = width * (1f - value)
        val pulse = if (isPulsing) {
            pulse.current / 2f + 0.25f
        } else {
            1.0f
        }

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.75f * pulse)
        UtilsGraphics.drawQuad(x + 0, y, a, height, true, false)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.25f * pulse)
        UtilsGraphics.drawQuad(x + a, y, b, height, true, false)

        label.render()
    }

    fun remove() {
        label.delete()
    }

}
