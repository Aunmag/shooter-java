package aunmag.shooter.core.gui;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.core.utilities.UtilsGraphics;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Page {

    public static final PageStack STACK = new PageStack();

    @Nullable
    public Texture wallpaper;
    public Vector4f color = new Vector4f(0.2f, 0.2f, 0.2f, 0.6f);
    private List<Label> elements = new ArrayList<>();

    public Page(@Nullable Texture wallpaper) {
        this.wallpaper = wallpaper;
    }

    public void open() {
        STACK.open(this);
    }

    public void add(Label label) {
        elements.add(label);
    }

    public void update() {
        for (var element: elements) {
            element.update();
        }
    }

    public void render() {
        renderWallpaper();
        fillScreen();

        for (var element: elements) {
            element.render();
        }
    }

    private void renderWallpaper() {
        if (wallpaper != null) {
            var projection = Application.getWindow().projection;
            Application.getShader().setUniformProjection(projection);
            wallpaper.bind();
            wallpaper.render();
        }
    }

    private void fillScreen() {
        UtilsGraphics.setDrawColor(color);
        UtilsGraphics.drawPrepare();
        UtilsGraphics.fillScreen();
    }

}
