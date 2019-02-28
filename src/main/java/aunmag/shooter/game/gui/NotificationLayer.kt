package aunmag.shooter.game.gui

import aunmag.shooter.core.utilities.OperativeManager
import aunmag.shooter.core.utilities.TimeFlow

class NotificationLayer(
        private val time: TimeFlow
) {

    private val manager = OperativeManager<Notification>()

    internal fun add(title: String, details: String) {
        manager.all.add(Notification(time, title, details))
    }

    internal fun update() {
        manager.update()
    }

    internal fun render() {
        manager.render()
    }

    internal fun clear() {
        manager.remove()
    }

}
