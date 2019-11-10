package aunmag.shooter.game.client.player;

import aunmag.shooter.core.graphics.Graphics;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.client.Context;
import org.lwjgl.opengl.GL11;

public class Crosshair {

    private static final int size = 5;

    public void render() {
        Context.main.getPlayerActor().ifPresent(shooter -> {
            if (shooter.isAiming.isTurnedOff()) {
                return;
            }

            var window = Context.main.getWindow();
            var camera = Context.main.getCamera();

            var degree = shooter.isAiming.get();
            var radians = shooter.body.radians;

            var cos = (float) Math.cos(radians);
            var sin = (float) Math.sin(radians);
            var distance = camera.toMeters(window.getCenterY() * degree);
            var x = shooter.body.position.x + (distance + shooter.body.radius) * cos;
            var y = shooter.body.position.y + (distance + shooter.body.radius) * sin;

            var offset = camera.toMeters(size);
            var offsetX1 = offset * (float) Math.cos(radians + UtilsMath.PIx0_5);
            var offsetY1 = offset * (float) Math.sin(radians + UtilsMath.PIx0_5);
            var offsetX2 = offsetX1 * 3;
            var offsetY2 = offsetY1 * 3;

            var alpha = UtilsMath.limit(distance, 0, 1);
            GL11.glColor4f(1f, 1f, 1f, alpha);

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

            GL11.glLineStipple(size, (short) 0xAAAA);
            GL11.glEnable(GL11.GL_LINE_STIPPLE);
            var x2 = x - distance * cos;
            var y2 = y - distance * sin;
            Graphics.draw.line(x, y, x2, y2, camera::project);
            GL11.glDisable(GL11.GL_LINE_STIPPLE);
        });
    }

}
