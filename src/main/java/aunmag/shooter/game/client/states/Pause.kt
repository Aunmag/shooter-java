package aunmag.shooter.game.client.states

import aunmag.shooter.core.Application
import aunmag.shooter.core.gui.Button
import aunmag.shooter.core.gui.Grid
import aunmag.shooter.core.gui.Label
import aunmag.shooter.core.gui.Page
import aunmag.shooter.core.gui.font.FontStyle
import aunmag.shooter.core.structures.Texture
import aunmag.shooter.core.utilities.UtilsAudio
import aunmag.shooter.game.client.App
import aunmag.shooter.game.client.Constants
import org.joml.Vector4f

class Pause {

    val buttonContinue = Button(4, 6, 4, 1, "Continue", Button.ACTION_BACK)
    private val theme = UtilsAudio.getOrCreateSoundOgg("sounds/music/menu")

    val settingsPage: SettingsPage = SettingsPage()

    init {
        theme.setIsLooped(true)
        buttonContinue.isEnabled = false
        createPageMain()

        Page.STACK.setOnQuit{App.main.isPause = false}
    }

    private fun createPageMain() {
        val wallpaper = Texture.getOrCreate(
                "images/wallpapers/main_menu", Texture.Type.WALLPAPER
        )
        val page = Page(wallpaper)

        page.add(Label(3, 3, 6, 1, Constants.TITLE))

        val version = Label(
                Grid(24),
                11, 8, 2, 1,
                "v" + Constants.VERSION,
                FontStyle.LABEL_LIGHT
        )
        version.setTextColour(Vector4f(1f, 1f, 1f, 0.75f))

        page.add(version)
        page.add(buttonContinue)
        page.add(Button(4, 7, 4, 1, "New game") { App.main.newGame() })
        page.add(Button(4, 8, 4, 1, "Settings",
                           settingsPage.createPageSettings()::open))
        page.add(Button(4, 9, 4, 1, "Help", createPageHelp()::open))
        page.add(Button(4, 10, 4, 1, "Exit", createPageExit()::open))

        page.open()
    }

    private fun createPageHelp(): Page {
        val wallpaper = Texture.getOrCreate(
                "images/wallpapers/help", Texture.Type.WALLPAPER
        )
        val page = Page(wallpaper)
        val style = FontStyle.LABEL_LIGHT

        page.add(Label(5, 1, 2, 1, "Help"))
        page.add(Label(4, 3, 1, 1, "Movement", style))
        page.add(Label(7, 3, 1, 1, "W, A, S, D", style))
        page.add(Label(4, 4, 1, 1, "Rotation", style))
        page.add(Label(7, 4, 1, 1, "Mouse", style))
        page.add(Label(4, 5, 1, 1, "Sprint", style))
        page.add(Label(7, 5, 1, 1, "Shift", style))
        page.add(Label(4, 6, 1, 1, "Attack", style))
        page.add(Label(7, 6, 1, 1, "LMB", style))
        page.add(Label(4, 7, 1, 1, "Zoom in/out", style))
        page.add(Label(7, 7, 1, 1, "+/-", style))
        page.add(Label(4, 8, 1, 1, "Menu", style))
        page.add(Label(7, 8, 1, 1, "Escape", style))
        page.add(Button(4, 10, 4, 1, "Back", Button.ACTION_BACK))

        return page
    }

    private fun createPageExit(): Page {
        val wallpaper = Texture.getOrCreate(
                "images/wallpapers/exit", Texture.Type.WALLPAPER
        )
        val page = Page(wallpaper)

        page.add(Label(3, 3, 6, 1, "Are you sure you want to exit?"))
        page.add(Button(4, 8, 4, 1, "Yes") { Application.stopRunning() })
        page.add(Button(4, 9, 4, 1, "No", Button.ACTION_BACK))

        return page
    }

    fun resume() {
        theme.play()
    }

    fun suspend() {
        theme.stop()
    }

    fun update() {
        Page.STACK.update()
    }

    fun render() {
        Page.STACK.render()
    }

}
