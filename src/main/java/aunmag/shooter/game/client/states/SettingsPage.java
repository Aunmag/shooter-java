package aunmag.shooter.game.client.states;

import aunmag.shooter.core.gui.font.FontStyle;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.core.gui.Button;
import aunmag.shooter.core.gui.Label;
import aunmag.shooter.core.gui.Page;
import aunmag.shooter.game.client.App;
import aunmag.shooter.game.environment.actor.ActorType;

public class SettingsPage {
    // ScenarioEncircling actorType select buttons
    private final Button[] actorSelectButtons = {
        new Button(5, 2, 1, 1, "Classic", () -> {
            changeActorTypeOrAccept(0,
                    ScenarioStatus.scenarioEncircling,
                    ActorType.human);
        }),
        new Button(7, 2, 1, 1, "Cowboy", () -> {
            changeActorTypeOrAccept(1,
                    ScenarioStatus.scenarioEncircling,
                    ActorType.humanCowboy);
        }),
    };

    public SettingsPage() {
        // The first type is initially selected
        actorSelectButtons[0].setEnabled(false);
    }

    public Page createPageSettings() {
        Texture wallpaper = Texture.getOrCreate(
                "images/wallpapers/main_menu", Texture.Type.WALLPAPER
        );
        Page page = new Page(wallpaper);
        FontStyle style = FontStyle.LABEL_LIGHT;

        page.add(new Label(5, 1, 2, 1, "Settings"));
        page.add(new Label(3, 2, 1, 1, "Select Player", style));
        for (Button button : actorSelectButtons) {
            page.add(button);
        }

        page.add(new Button(4, 10, 4, 1, "Back", Button.ACTION_BACK));

        return page;
    }

    // Either change the actor or opens a confirmation page
    public void changeActorTypeOrAccept(
            // `actorSelectButtons` index of button, that will be unavaliable
            int indexOfUnavaliableButton,
            ScenarioStatus scenarioStatus,
            ActorType newActorType
    ) {
        if (App.main.getGame() != null) {
            createActorChangeConfirmingPage(
                    indexOfUnavaliableButton,
                    scenarioStatus,
                    newActorType
            ).open();
        } else {
            changeActorType(scenarioStatus, newActorType);
            setUnavaliableButton(indexOfUnavaliableButton);
        }
    }

    public Page createActorChangeConfirmingPage(
            int indexOfUnavaliableButton,
            ScenarioStatus scenarioStatus,
            ActorType newType
    ) {
        Texture wallpaper = Texture.getOrCreate(
                "images/wallpapers/main_menu", Texture.Type.WALLPAPER
        );
        Page page = new Page(wallpaper);
        page.add(new Label(5, 1, 1, 1,
                "When you change the character, the current game will be lost"));
        page.add(new Label(5, 2, 1, 1, "Continue?"));

        page.add(new Button(4, 4, 2, 1, "Yes", () -> {
            App.main.endGame();
            changeActorType(scenarioStatus, newType);
            setUnavaliableButton(indexOfUnavaliableButton);
            Button.ACTION_BACK.run();
        }));
        page.add(new Button(6, 4, 2, 1, "No", Button.ACTION_BACK));

        return page;
    }

    private void changeActorType(
            ScenarioStatus scenarioStatus,
            ActorType newType
    ) {
        scenarioStatus.actorType = newType;
        if (App.main.getGame() != null) {
            App.main.getGame().getPlayer().getActor().setType(newType);
        }
    }

    // Sets the button pressed in `actorSelectButtons`
    public void setUnavaliableButton(int index) {
        for (int i = 0; i < actorSelectButtons.length; i++) {
            Button curButton = actorSelectButtons[i];
            if (index != i) {
                curButton.setEnabled(true);
            } else {
                curButton.setEnabled(false);
            }
        }
    }
}
