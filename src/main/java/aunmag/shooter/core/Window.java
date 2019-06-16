package aunmag.shooter.core;

import aunmag.shooter.core.basics.BaseQuad;
import aunmag.shooter.game.client.Constants;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Window extends BaseQuad {

    public static final int UNDEFINED_ID = 0;
    public final long id;
    public final Matrix4fc projection;
    private boolean isCursorGrabbed = false;
    private boolean isInitialized = false;

    Window() {
        super(1024, 576);

        long monitorId = GLFW.glfwGetPrimaryMonitor();
        int monitorSizeX = GLFW.glfwGetVideoMode(monitorId).width();
        int monitorSizeY = GLFW.glfwGetVideoMode(monitorId).height();

        if (Configs.isFullscreen()) {
            setSize(monitorSizeX, monitorSizeY);
        }

        int sizeX = (int) getWidth();
        int sizeY = (int) getHeight();

        projection = new Matrix4f().setOrtho2D(
                -getCenterX(),
                +getCenterX(),
                -getCenterY(),
                +getCenterY()
        );

        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, Configs.getAntialiasing());

        id = GLFW.glfwCreateWindow(
                sizeX,
                sizeY,
                Constants.TITLE, // TODO: Do not access game package!
                Configs.isFullscreen() ? monitorId : 0,
                0
        );

        if (id == UNDEFINED_ID) {
            throw new IllegalStateException("Failed to create window!");
        }

        if (!Configs.isFullscreen()) {
            int centerX = (monitorSizeX - sizeX) / 2;
            int centerY = (monitorSizeY - sizeY) / 2;
            GLFW.glfwSetWindowPos(id, centerX, centerY);
        }

        GLFW.glfwShowWindow(id);
        GLFW.glfwMakeContextCurrent(id);

        isInitialized = true;
    }

    public Vector2f project(float x, float y) {
        x = (x - getCenterX() + 1) / getCenterX();
        y = (getCenterY() - y - 1) / getCenterY();
        return new Vector2f(x, y);
    }

    public void setCursorGrabbed(boolean isCursorGrabbed) {
        if (isCursorGrabbed == this.isCursorGrabbed) {
            return;
        } else {
            this.isCursorGrabbed = isCursorGrabbed;
        }

        if (isCursorGrabbed) {
            GLFW.glfwSetInputMode(id, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
            GLFW.glfwSetInputMode(id, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        } else {
            GLFW.glfwSetInputMode(id, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }

    /* Setters */

    protected void setSize(float width, float height) {
        if (isInitialized) {
            String message = "Unable to change window size after initialization";
            System.err.println(message);
        } else {
            super.setSize(width, height);
        }
    }

}
