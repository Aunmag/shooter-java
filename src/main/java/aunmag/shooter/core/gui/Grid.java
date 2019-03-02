package aunmag.shooter.core.gui;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.utilities.UtilsGraphics;
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

        for (var n = 0; n < slices; n++) {
            var x = n * stepX;
            var y = n * stepY;

            UtilsGraphics.drawLine(
                    x, 0,
                    x, Application.getWindow().getHeight(),
                    false
            );

            UtilsGraphics.drawLine(
                    0, y,
                    Application.getWindow().getWidth(), y,
                    false
            );
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
