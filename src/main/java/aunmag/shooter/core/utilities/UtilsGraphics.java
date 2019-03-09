package aunmag.shooter.core.utilities;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.basics.BaseQuad;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public final class UtilsGraphics {

    public static final Vector4f COLOR_WHITE = new Vector4f(1f, 1f, 1f, 1f);

    private UtilsGraphics() {}

    public static void drawPrepare() {
        GL20.glUseProgram(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public static void drawLine(
            float x1,
            float y1,
            float x2,
            float y2,
            boolean isOnWorld
    ) {
        Vector2f a;
        Vector2f b;

        if (isOnWorld) {
            a = Application.getCamera().calculateViewPosition(x1, y1);
            b = Application.getCamera().calculateViewPosition(x2, y2);
        } else {
            a = Application.getWindow().calculateViewPosition(x1, y1);
            b = Application.getWindow().calculateViewPosition(x2, y2);
        }

        drawLine(a.x(), a.y(), b.x(), b.y());
    }

    private static void drawLine(float x1, float y1, float x2, float y2) {
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
    }

    public static void drawQuad(BaseQuad quad, boolean isFilled, boolean isOnWorld) {
        drawQuad(
                quad.getPosition().x(),
                quad.getPosition().y(),
                quad.getWidth(),
                quad.getHeight(),
                isFilled,
                isOnWorld
        );
    }

    public static void drawQuad(
            float x,
            float y,
            float width,
            float height,
            boolean isFilled,
            boolean isOnWorld
    ) {
        if (isFilled) {
            x -= 1;
            y -= 1;
        }

        float x1;
        float y1;
        float x2;
        float y2;

        {
            Vector2f a;
            Vector2f b;

            if (isOnWorld) {
                a = Application.getCamera().calculateViewPosition(x, y);
                b = Application.getCamera().calculateViewPosition(x + width, y + height);
            } else {
                a = Application.getWindow().calculateViewPosition(x, y);
                b = Application.getWindow().calculateViewPosition(x + width, y + height);
            }

            x1 = a.x();
            y1 = a.y();
            x2 = b.x();
            y2 = b.y();
        }

        if (isFilled) {
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2f(x1, y1); // 1. a - top left
            GL11.glVertex2f(x1, y2); // 1. d - down left
            GL11.glVertex2f(x2, y1); // 1. b - top right
            GL11.glVertex2f(x2, y1); // 2. b - top right
            GL11.glVertex2f(x1, y2); // 2. d - down left
            GL11.glVertex2f(x2, y2); // 2. c - down right
            GL11.glEnd();
        } else {
            drawLine(x1, y1, x2, y1);
            drawLine(x2, y1, x2, y2);
            drawLine(x2, y2, x1, y2);
            drawLine(x1, y2, x1, y1);
        }
    }

    public static void fillScreen() {
        final float n = 1f;
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2f(-n, +n); // 1. a - top left
        GL11.glVertex2f(-n, -n); // 1. d - down left
        GL11.glVertex2f(+n, +n); // 1. b - top right
        GL11.glVertex2f(+n, +n); // 2. b - top right
        GL11.glVertex2f(-n, -n); // 2. d - down left
        GL11.glVertex2f(+n, -n); // 2. c - down right
        GL11.glEnd();
    }

    public static void drawCircle(
            float x,
            float y,
            float radius,
            boolean isFilled,
            boolean isOnWorld
    ) {
        GL11.glBegin(isFilled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_LOOP);

        final float accuracy = 0.4f;
        for (var radians = 0.0; radians <= UtilsMath.PIx2; radians += accuracy) {
            float fragmentX = (float) (x + radius * Math.cos(radians));
            float fragmentY = (float) (y + radius * Math.sin(radians));

            if (isOnWorld) {
                Vector2f viewPosition = Application.getCamera().calculateViewPosition(
                        fragmentX,
                        fragmentY
                );
                fragmentX = viewPosition.x();
                fragmentY = viewPosition.y();
            }

            GL11.glVertex2f(fragmentX, fragmentY);
        }
        GL11.glEnd();
    }

    public static void setDrawColor(Vector3f color) {
        GL11.glColor3f(color.x(), color.y(), color.z());
    }

    public static void setDrawColor(Vector4f color) {
        GL11.glColor4f(color.x(), color.y(), color.z(), color.w());
    }

}
