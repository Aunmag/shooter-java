package aunmag.shooter.game.client.states;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.audio.Source;
import aunmag.shooter.core.gui.Button;
import aunmag.shooter.core.gui.Grid;
import aunmag.shooter.core.gui.Label;
import aunmag.shooter.core.gui.Page;
import aunmag.shooter.core.gui.font.FontStyle;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.core.utilities.Operative;
import aunmag.shooter.core.utilities.UtilsAudio;
import aunmag.shooter.game.client.Constants;
import aunmag.shooter.game.client.Context;
import aunmag.shooter.game.environment.actor.ActorType;
import aunmag.shooter.game.scenarios.ScenarioEncircling;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

public class Pause extends Operative {

    @Nullable
    public final Source theme = UtilsAudio.provideSound("sounds/music/menu");
    private final Button buttonContinue
            = new Button(4, 7, 4, 1, "Continue", Button.ACTION_BACK);

    public Pause() {
        if (theme != null) {
            theme.setLooped(true);
        }

        createPageMain().open();

        Page.STACK.setOnQuit(() -> Context.main.application.setPause(false));
    }

    private Page createPageMain() {
        var wallpaper = Texture.manager
                .asWallpaper()
                .provide("images/wallpapers/main_menu");

        var version = new Label(
                new Grid(24),
                11, 8, 2, 1,
                "v" + Constants.VERSION,
                FontStyle.LABEL_LIGHT
        );

        version.setTextColour(new Vector4f(1, 1, 1, 0.75f));

        var page = new Page(wallpaper);
        page.add(new Label(3, 3, 6, 1, Constants.TITLE));
        page.add(version);
        page.add(buttonContinue);
        page.add(new Button(4, 8, 4, 1, "New game",
                createCharacterSelectionPage()::open));
        page.add(new Button(4, 9, 4, 1, "Help", createPageHelp()::open));
        page.add(new Button(4, 10, 4, 1, "Exit", createPageExit()::open));

        return page;
    }

    private Page createCharacterSelectionPage() {
        var wallpaper = Texture.manager
                .asWallpaper()
                .provide("images/wallpapers/main_menu");
        var page = new Page(wallpaper);

        page.add(new Label(3, 3, 6, 1, "Select your character"));
        page.add(new Button(4, 7, 4, 1, "Soldier", () -> {
            ScenarioEncircling.selectedActor = ActorType.soldier;
            Context.main.application.newGame();
            Page.STACK.back();
        }));
        page.add(new Button(4, 8, 4, 1, "Bandit", () -> {
            ScenarioEncircling.selectedActor = ActorType.bandit;
            Context.main.application.newGame();
            Page.STACK.back();
        }));

        return page;
    }

    private Page createPageHelp() {
        var wallpaper = Texture.manager.asWallpaper().provide("images/wallpapers/help");
        var page = new Page(wallpaper);
        var style = FontStyle.LABEL_LIGHT;

        page.add(new Label(5, 1, 2, 1, "Help"));
        page.add(new Label(4, 3, 1, 1, "Movement", style));
        page.add(new Label(7, 3, 1, 1, "W, A, S, D", style));
        page.add(new Label(4, 4, 1, 1, "Rotation", style));
        page.add(new Label(7, 4, 1, 1, "Mouse", style));
        page.add(new Label(4, 5, 1, 1, "Sprint", style));
        page.add(new Label(7, 5, 1, 1, "Shift", style));
        page.add(new Label(4, 6, 1, 1, "Attack", style));
        page.add(new Label(7, 6, 1, 1, "LMB", style));
        page.add(new Label(4, 7, 1, 1, "Zoom in/out", style));
        page.add(new Label(7, 7, 1, 1, "+/-", style));
        page.add(new Label(4, 8, 1, 1, "Menu", style));
        page.add(new Label(7, 8, 1, 1, "Escape", style));
        page.add(new Button(4, 10, 4, 1, "Back", Button.ACTION_BACK));

        return page;
    }

    private Page createPageExit() {
        var wallpaper = Texture.manager.asWallpaper().provide("images/wallpapers/exit");
        var page = new Page(wallpaper);

        page.add(new Label(3, 3, 6, 1, "Are you sure you want to exit?"));
        page.add(new Button(4, 8, 4, 1, "Yes", Application::stopRunning));
        page.add(new Button(4, 9, 4, 1, "No", Button.ACTION_BACK));

        return page;
    }

    public void resume() {
        if (theme != null) {
            theme.play();
        }
    }

    public void suspend() {
        if (theme != null) {
            theme.pause();
        }
    }

    @Override
    public void update() {
        buttonContinue.setEnabled(Context.main.application.getGame() != null);
        Page.STACK.update();
    }

    @Override
    public void render() {
        Page.STACK.render();
    }

}
