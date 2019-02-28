package aunmag.shooter.game.gui

import aunmag.shooter.core.font.FontStyleDefault
import aunmag.shooter.core.gui.GuiLabel
import aunmag.shooter.core.utilities.Operative
import aunmag.shooter.core.utilities.TimeFlow
import aunmag.shooter.core.utilities.Timer
import aunmag.shooter.core.utilities.UtilsMath

internal class Notification(
        time: TimeFlow,
        title: String,
        details: String
) : Operative() {

    private val timeFadeIn = 0.125f
    private val timeFadeOut = 0.5f
    private val timer = Timer(time, 3.0)
    private val title = GuiLabel(5, 4, 2, 1, title)
    private val details = GuiLabel(5, 5, 2, 1, details, FontStyleDefault.labelLight)

    init {
        timer.next()
    }

    override fun update() {
        if (timer.isDone) {
            remove()
        }
    }

    override fun render() {
        val timeInitial = timer.target - timer.duration
        val timePassed = timer.time.current - timeInitial
        val timeRemain = timer.target - timer.time.current
        val timeFade = if (timePassed < timeRemain) {
            timePassed / timeFadeIn
        } else {
            timeRemain / timeFadeOut
        }.toFloat()

        val alpha = UtilsMath.limitNumber(timeFade, 0f, 1f)

        title.setTextColour(1f, 1f, 1f, alpha)
        details.setTextColour(1f, 1f, 1f, alpha)

        title.render()
        details.render()
    }

    override fun remove() {
        if (isRemoved) {
            return
        }

        title.delete()
        details.delete()

        super.remove()
    }
}
