package aunmag.shooter.core.basics;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.utilities.UtilsGraphics;
import org.lwjgl.opengl.GL11;

public class BaseGrid {

    public static BaseGrid grid12 = new BaseGrid(12);
    public static BaseGrid grid24 = new BaseGrid(24);

    private final int slices;
    private float stepX;
    private float stepY;

    public BaseGrid(int slices) {
        this.slices = slices;
        stepX = calculateStepX();
        stepY = calculateStepY();
    }

    private float calculateStepX() {
        return Application.getWindow().getWidth() / (float) slices;
    }

    private float calculateStepY() {
        return Application.getWindow().getHeight() / (float) slices;
    }

    public void render() {
        GL11.glColor4f(1f, 1f, 1f, 0.2f);

        for (var n = 0.0; n < slices; n++) {
            UtilsGraphics.drawLine(
                    (float) (stepX * n), 0,
                    (float) (stepX * n), Application.getWindow().getHeight(),
                    false
            );

            UtilsGraphics.drawLine(
                    0, (float) (stepY * n),
                    Application.getWindow().getWidth(), (float) (stepY * n),
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
