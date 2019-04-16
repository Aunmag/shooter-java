package aunmag.shooter.game.client.states;

import aunmag.shooter.core.gui.font.Font;
import aunmag.shooter.core.gui.font.FontStyle;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.core.gui.Button;
import aunmag.shooter.core.gui.Label;
import aunmag.shooter.core.gui.Page;

public class SettingsPage {

    private final Button[] crosshairModeSelectButtons = {
        new Button(3, 2, 2, 1, "Press", () -> {
            ScenarioStatus.crosshairControl = ScenarioStatus.CrosshairControl.PRESS;
            setUnavaliableButton(0, 0);
        }),
        new Button(5, 2, 2, 1, "Hold", () -> {
            ScenarioStatus.crosshairControl = ScenarioStatus.CrosshairControl.HOLD;
            setUnavaliableButton(0, 1);
        })
    };

    private final Button[][] buttonGroups = {
        crosshairModeSelectButtons
    };

    public SettingsPage() {
        setUnavaliableButton(0, 0);
    }

    public Page createPageSettings() {
        Texture wallpaper = Texture.getOrCreate(
                "images/wallpapers/main_menu", Texture.Type.WALLPAPER
        );
        Page page = new Page(wallpaper);
        page.add(new Label(5, 1, 2, 1, "Settings"));

        page.add(new Label(1, 2, 2, 1, "Crosshair",
                new FontStyle(Font.DEFAULT, 3, true)));

        for (Button b: crosshairModeSelectButtons) {
            page.add(b);
        }

        page.add(new Button(4, 10, 4, 1, "Back", Button.ACTION_BACK));

        return page;
    }

    private void setUnavaliableButton(int buttonGroupIndex, int buttonIndex) {
        Button[] buttonGroup = buttonGroups[buttonGroupIndex];
        for (int i = 0; i < buttonGroup.length; i++) {
            if (i != buttonIndex) {
                buttonGroup[i].setEnabled(true);
            } else {
                buttonGroup[i].setEnabled(false);
            }
        }
    }
}
