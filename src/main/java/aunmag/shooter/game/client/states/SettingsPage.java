package aunmag.shooter.game.client.states;

import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.core.gui.Button;
import aunmag.shooter.core.gui.Label;
import aunmag.shooter.core.gui.Page;

public class SettingsPage {

    public Page createPageSettings() {
        Texture wallpaper = Texture.getOrCreate(
                "images/wallpapers/main_menu", Texture.Type.WALLPAPER
        );
        Page page = new Page(wallpaper);
        // FontStyle style = FontStyle.LABEL_LIGHT;
        page.add(new Label(5, 1, 2, 1, "Settings"));

        page.add(new Button(4, 10, 4, 1, "Back", Button.ACTION_BACK));

        return page;
    }
}
