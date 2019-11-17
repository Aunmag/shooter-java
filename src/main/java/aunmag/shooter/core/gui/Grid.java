package aunmag.shooter.core.gui;

import aunmag.shooter.core.Context;
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
        stepX = Context.main.getWindow().getSizeX() / slices;
        stepY = Context.main.getWindow().getSizeY() / slices;
    }

    public void render() {
        GL11.glColor4f(1f, 1f, 1f, 0.2f);
        var window = Context.main.getWindow();

        for (var n = 0; n < slices; n++) {
            var x = n * stepX;
            var y = n * stepY;

            Graphics.draw.line(x, 0, x, window.getSizeY(), window::project);
            Graphics.draw.line(0, y, window.getSizeX(), y, window::project);
        }
    }

    public float getStepX() {
        return stepX;
    }

    public float getStepY() {
        return stepY;
    }

}
