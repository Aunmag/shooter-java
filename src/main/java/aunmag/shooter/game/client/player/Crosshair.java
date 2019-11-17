package aunmag.shooter.game.client.player;

import aunmag.shooter.core.graphics.Graphics;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.client.Context;
import org.lwjgl.opengl.GL11;

public class Crosshair {

    private static final int WIDTH = 5;
    private static final float LENGTH_FACTOR = 0.75F;

    public void render() {
        Context.main.getPlayerActor().ifPresent(shooter -> {
            var degree = shooter.isAiming.get();
            if (degree == 0) {
                return;
            }

            var window = Context.main.getWindow();
            var camera = Context.main.getCamera();
            var direction = shooter.body.radians;
            var cos = (float) Math.cos(direction);
            var sin = (float) Math.sin(direction);
            var length = camera.toMeters(window.getSizeY() * degree * LENGTH_FACTOR);
            var x = shooter.body.position.x + (length + shooter.body.radius) * cos;
            var y = shooter.body.position.y + (length + shooter.body.radius) * sin;

            GL11.glColor4f(1, 1, 1, degree);
            GL11.glLineStipple(WIDTH, (short) 0xAAAA);
            GL11.glEnable(GL11.GL_LINE_STIPPLE);
            Graphics.draw.line(
                    x,
                    y,
                    x - length * cos,
                    y - length * sin,
                    camera::project
            );
            GL11.glDisable(GL11.GL_LINE_STIPPLE);

            var offset = camera.toMeters(WIDTH);
            var offsetX1 = offset * (float) Math.cos(direction + UtilsMath.PIx0_5);
            var offsetY1 = offset * (float) Math.sin(direction + UtilsMath.PIx0_5);
            var offsetX2 = offsetX1 * 3;
            var offsetY2 = offsetY1 * 3;

            Graphics.draw.line(
                    x + offsetX1,
                    y + offsetY1,
                    x + offsetX2,
                    y + offsetY2,
                    camera::project
            );

            Graphics.draw.line(
                    x - offsetX1,
                    y - offsetY1,
                    x - offsetX2,
                    y - offsetY2,
                    camera::project
            );
        });
    }

}
