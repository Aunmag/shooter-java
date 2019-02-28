package aunmag.shooter.game.gui

import aunmag.shooter.core.Application
import aunmag.shooter.core.basics.BaseGrid
import aunmag.shooter.core.font.FontStyleDefault
import aunmag.shooter.core.gui.GuiLabel
import aunmag.shooter.core.utilities.FluidToggle
import aunmag.shooter.core.utilities.UtilsGraphics
import org.lwjgl.opengl.GL11

class Parameter(title: String, var value: Float, x: Int, y: Int) {

    companion object {
        private val grid = BaseGrid(36)
        private val pulse = FluidToggle(Application.time, 0.2)

        fun update() {
            pulse.update()
            if (pulse.isTargetReached) {
                pulse.toggle()
            }
        }
    }

    var isPulsing = false
    private val label = GuiLabel(grid, x, y, 1, 1, title, FontStyleDefault.labelLight)

    init {
        label.setTextColour(1.0f, 1.0f, 1.0f, 0.5f)
    }

    fun render() {
        val width = grid.stepX * 3.0f
        val height = grid.stepY / 2.0f
        val x = grid.stepX * (label.position.x + 2.0f)
        val y = grid.stepY * (label.position.y + 0.3f)
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
