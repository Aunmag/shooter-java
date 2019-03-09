package aunmag.shooter.game.gui

import aunmag.shooter.core.gui.Label
import aunmag.shooter.core.gui.font.FontStyle
import aunmag.shooter.core.utilities.Operative
import aunmag.shooter.core.utilities.TimeFlow
import aunmag.shooter.core.utilities.Timer
import aunmag.shooter.core.utilities.UtilsMath
import org.joml.Vector4f

internal class Notification(
        time: TimeFlow,
        title: String,
        details: String
) : Operative() {

    private val timeFadeIn = 0.125f
    private val timeFadeOut = 0.5f
    private val timer = Timer(time, 3.0)
    private val title = Label(5, 4, 2, 1, title)
    private val details = Label(5, 5, 2, 1, details, FontStyle.LABEL_LIGHT)

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
        val color = Vector4f(1f, 1f, 1f, alpha)

        title.setTextColour(color)
        details.setTextColour(color)

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
