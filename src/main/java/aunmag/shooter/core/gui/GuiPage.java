package aunmag.shooter.core.gui;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.core.utilities.UtilsGraphics;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

import java.util.ArrayList;

public class GuiPage {

    private ArrayList<GuiLabel> labels = new ArrayList<>();
    private ArrayList<GuiButton> buttons = new ArrayList<>();
    @Nullable public Texture wallpaper;
    public final Vector4f color = new Vector4f(0.2f, 0.2f, 0.2f, 0.6f);

    public GuiPage(@Nullable Texture wallpaper) {
        this.wallpaper = wallpaper;
    }

    public void open() {
        GuiManager.open(this);
    }

    public void add(GuiLabel label) {
        labels.add(label);
    }

    public void add(GuiButton button) {
        buttons.add(button);
    }

    public void update() {
        for (GuiButton button: buttons) {
            button.update();
        }
    }

    public void render() {
        renderWallpaper();
        fillScreen();

        for (GuiLabel label: labels) {
            label.render();
        }

        for (GuiButton button: buttons) {
            button.render();
        }
    }

    private void renderWallpaper() {
        if (wallpaper == null) {
            return;
        }

        Application.getShader().setUniformProjection(Application.getWindow().projection);
        wallpaper.bind();
        wallpaper.render();
    }

    private void fillScreen() {
        UtilsGraphics.setDrawColor(color);
        UtilsGraphics.drawPrepare();
        UtilsGraphics.fillScreen();
    }

}
