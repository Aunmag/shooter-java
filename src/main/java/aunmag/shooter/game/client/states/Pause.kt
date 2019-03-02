package aunmag.shooter.game.client.states

import aunmag.shooter.core.Application
import aunmag.shooter.core.font.FontStyleDefault
import aunmag.shooter.core.gui.*
import aunmag.shooter.core.structures.Texture
import aunmag.shooter.core.utilities.UtilsAudio
import aunmag.shooter.game.client.App
import aunmag.shooter.game.client.Constants

class Pause {

    val buttonContinue = GuiButton(4, 7, 4, 1, "Continue", GuiButton.actionBack)
    private val theme = UtilsAudio.getOrCreateSoundOgg("sounds/music/menu")

    init {
        theme.setIsLooped(true)
        buttonContinue.setIsAvailable(false)
        createPageMain()
    }

    private fun createPageMain() {
        val wallpaper = Texture.getOrCreate(
                "images/wallpapers/main_menu", Texture.Type.WALLPAPER
        )
        val page = GuiPage(wallpaper)

        page.add(GuiLabel(3, 3, 6, 1, Constants.TITLE))
        // TODO: Change
        page.add(GuiLabel(
                Grid(24),
                6, 8, 12, 1,
                "v " + Constants.VERSION,
                FontStyleDefault.labelLight
        ))
        page.add(GuiLabel(
                Grid(24),
                6, 9, 12, 1,
                " by " + Constants.DEVELOPER,
                FontStyleDefault.labelLight
        ))
        page.add(buttonContinue)
        page.add(GuiButton(4, 8, 4, 1, "New game") { App.main.newGame() })
        page.add(GuiButton(4, 9, 4, 1, "Help", createPageHelp()::open))
        page.add(GuiButton(4, 10, 4, 1, "Exit", createPageExit()::open))

        page.open()
    }

    private fun createPageHelp(): GuiPage {
        val wallpaper = Texture.getOrCreate(
                "images/wallpapers/help", Texture.Type.WALLPAPER
        )
        val page = GuiPage(wallpaper)
        val style = FontStyleDefault.labelLight

        page.add(GuiLabel(5, 1, 2, 1, "Help"))
        page.add(GuiLabel(4, 3, 1, 1, "Movement", style))
        page.add(GuiLabel(7, 3, 1, 1, "W, A, S, D", style))
        page.add(GuiLabel(4, 4, 1, 1, "Rotation", style))
        page.add(GuiLabel(7, 4, 1, 1, "Mouse", style))
        page.add(GuiLabel(4, 5, 1, 1, "Sprint", style))
        page.add(GuiLabel(7, 5, 1, 1, "Shift", style))
        page.add(GuiLabel(4, 6, 1, 1, "Attack", style))
        page.add(GuiLabel(7, 6, 1, 1, "LMB", style))
        page.add(GuiLabel(4, 7, 1, 1, "Zoom in/out", style))
        page.add(GuiLabel(7, 7, 1, 1, "+/-", style))
        page.add(GuiLabel(4, 8, 1, 1, "Menu", style))
        page.add(GuiLabel(7, 8, 1, 1, "Escape", style))
        page.add(GuiButton(4, 10, 4, 1, "Back", GuiButton.actionBack))

        return page
    }

    private fun createPageExit(): GuiPage {
        val wallpaper = Texture.getOrCreate(
                "images/wallpapers/exit", Texture.Type.WALLPAPER
        )
        val page = GuiPage(wallpaper)

        page.add(GuiLabel(3, 3, 6, 1, "Are you sure you want to exit?"))
        page.add(GuiButton(4, 8, 4, 1, "Yes") { Application.stopRunning() })
        page.add(GuiButton(4, 9, 4, 1, "No", GuiButton.actionBack))

        return page
    }

    fun resume() {
        GuiManager.activate()
        theme.play()
    }

    fun suspend() {
        theme.stop()
    }

    fun update() {
        GuiManager.update()

        if (GuiManager.isShouldClose()) {
            App.main.isPause = false
        }
    }

    fun render() {
        GuiManager.render()
    }

}
