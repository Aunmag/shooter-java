package aunmag.shooter.core.gui;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.graphics.Graphics;
import org.lwjgl.opengl.GL11;

public class Grid {

    public static final Grid GRID_12 = new Grid(12);

    private final int slices;
    private float stepX;
    private float stepY;

    public Grid(int slices) {
        this.slices = slices;
        refresh();
    }

    public void refresh() {
        stepX = Application.getWindow().getWidth() / slices;
        stepY = Application.getWindow().getHeight() / slices;
    }

    public void render() {
        GL11.glColor4f(1f, 1f, 1f, 0.2f);
        var window = Application.getWindow();

        for (var n = 0; n < slices; n++) {
            var x = n * stepX;
            var y = n * stepY;

            Graphics.draw.line(x, 0, x, window.getHeight(), window::project);
            Graphics.draw.line(0, y, window.getWidth(), y, window::project);
        }
    }

    /* Getters */

    public float getStepX() {
        return stepX;
    }

    public float getStepY() {
        return stepY;
    }

}
