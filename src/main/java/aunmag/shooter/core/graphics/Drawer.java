package aunmag.shooter.core.graphics;

import aunmag.shooter.core.utilities.UtilsMath;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Drawer {

    public static final int CIRCLE_FACES = 16;
    public static final float CIRCLE_ACCURACY = (float) (UtilsMath.PIx2 / CIRCLE_FACES);

    private void prepare() {
        GL20.glUseProgram(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public void line(float x1, float y1, float x2, float y2) {
        prepare();
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
    }

    public void line(float x1, float y1, float x2, float y2, Projector projector) {
        var a = projector.project(x1, y1);
        var b = projector.project(x2, y2);
        line(a.x, a.y, b.x, b.y);
    }

    public void quad(float x1, float y1, float x2, float y2, boolean fill) {
        if (fill) {
            prepare();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2f(x1, y1); // 1. a - top left
            GL11.glVertex2f(x1, y2); // 1. d - down left
            GL11.glVertex2f(x2, y1); // 1. b - top right
            GL11.glVertex2f(x2, y1); // 2. b - top right
            GL11.glVertex2f(x1, y2); // 2. d - down left
            GL11.glVertex2f(x2, y2); // 2. c - down right
            GL11.glEnd();
        } else {
            line(x1, y1, x2, y1);
            line(x2, y1, x2, y2);
            line(x2, y2, x1, y2);
            line(x1, y2, x1, y1);
        }
    }

    public void quad(
            float x1,
            float y1,
            float x2,
            float y2,
            boolean fill,
            Projector projector
    ) {
        var a = projector.project(x1, y1);
        var b = projector.project(x2, y2);
        quad(a.x, a.y, b.x, b.y, fill);
    }

    public void circle(
            float x,
            float y,
            float radius,
            boolean fill,
            Projector projector
    ) {
        prepare();
        GL11.glBegin(fill ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_LOOP);

        for (var radians = 0f; radians <= UtilsMath.PIx2; radians += CIRCLE_ACCURACY) {
            var fragmentX = x + radius * (float) Math.cos(radians);
            var fragmentY = y + radius * (float) Math.sin(radians);
            var projected = projector.project(fragmentX, fragmentY);
            GL11.glVertex2f(projected.x, projected.y);
        }

        GL11.glEnd();
    }

    public void fill() {
        quad(-1, -1, +1, +1, true);
    }

    public void withColor(Vector4f color) {
        GL11.glColor4f(color.x, color.y, color.z, color.w);
    }

}
