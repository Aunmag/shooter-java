package aunmag.shooter.core.input;

import aunmag.shooter.core.Context;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class Mouse {

    public static final float SCROLL_SMOOTH_MIN = 0.001f;

    private final Input input;
    private final boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    public final Vector2f position = new Vector2f(0, 0);
    public final Vector2f velocity = new Vector2f(0, 0);
    public float scroll = 0;
    public float scrollSmooth = 0;
    public float scrollSmoothness = 8f;

    public Mouse(Input input) {
        this.input = input;
        Arrays.fill(buttons, false);
        GLFW.glfwSetScrollCallback(input.window, this::onScroll);
    }

    public void update() {
        updateButtons();
        updatePosition();
        updateScroll();
    }

    protected void updateButtons() {
        for (int button = 0; button < buttons.length; button++) {
            buttons[button] = isButtonDown(button);
        }
    }

    protected void updatePosition() {
        var xBuffer = BufferUtils.createDoubleBuffer(1);
        var yBuffer = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(input.window, xBuffer, yBuffer);

        xBuffer.rewind();
        yBuffer.rewind();
        var x = (float) xBuffer.get();
        var y = (float) yBuffer.get();

        velocity.set(position.x - x, position.y - y);
        position.set(x, y);
    }

    protected void updateScroll() {
        scroll = 0;

        if (Math.abs(scrollSmooth) > SCROLL_SMOOTH_MIN) {
            scrollSmooth -= scrollSmooth * scrollSmoothness * Context.main.getDelta();
        } else {
            scrollSmooth = 0;
        }
    }

    protected void onScroll(long window, double x, double y) {
        scroll = (float) y;
        scrollSmooth += scroll;
    }

    public boolean isButtonDown(int button) {
        return GLFW.glfwGetMouseButton(input.window, button) == GLFW.GLFW_TRUE;
    }

    public boolean isButtonPressed(int button) {
        return isButtonDown(button) && !buttons[button];
    }

    public boolean isButtonReleased(int button) {
        return !isButtonDown(button) && buttons[button];
    }

}
