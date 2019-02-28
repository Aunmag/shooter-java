package aunmag.shooter.core.input;

import aunmag.shooter.core.Application;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;
import java.util.Arrays;

public class Mouse {

    private final boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    public final MouseWheel wheel = new MouseWheel();
    public final Vector2f position = new Vector2f(0, 0);
    public final Vector2f velocity = new Vector2f(0, 0);

    public Mouse() {
        Arrays.fill(buttons, false);
    }

    public void update() {
        wheel.update();
        updateButtons();
        updatePosition();
    }

    public void updateButtons() {
        if (Input.isAvailable()) {
            for (int button = 0; button < buttons.length; button++) {
                buttons[button] = isButtonDown(button);
            }
        }
    }

    public void updatePosition() {
        if (Input.isAvailable()) {
            DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
            GLFW.glfwGetCursorPos(Application.getWindow().id, xBuffer, yBuffer);

            xBuffer.rewind();
            yBuffer.rewind();
            float x = (float) xBuffer.get();
            float y = (float) yBuffer.get();

            velocity.set(position.x() - x, position.y() - y);
            position.set(x, y);
        } else {
            velocity.set(0, 0);
        }
    }

    public boolean isButtonDown(int button) {
        return Input.isAvailable() && GLFW.glfwGetMouseButton(
                Application.getWindow().id, button
        ) == GLFW.GLFW_TRUE;
    }

    public boolean isButtonPressed(int button) {
        return isButtonDown(button) && !buttons[button];
    }

    public boolean isButtonReleased(int button) {
        return !isButtonDown(button) && buttons[button];
    }

}
