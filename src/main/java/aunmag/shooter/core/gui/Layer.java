package aunmag.shooter.core.gui;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.graphics.Graphics;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.core.utilities.OperativeManager;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

public class Layer extends OperativeManager<Component> {

    @Nullable public Vector4f background = null;
    @Nullable public Texture wallpaper = null;

    public void add(Component component) {
        all.add(component);
    }

    @Override
    public void render() {
        if (wallpaper != null) {
            var projection = Application.getWindow().projection;
            Application.getShader().setUniformProjection(projection);
            wallpaper.bind();
            wallpaper.render();
        }

        if (background != null) {
            Graphics.draw.withColor(background);
            Graphics.draw.fill();
        }

        super.render();
    }

}
