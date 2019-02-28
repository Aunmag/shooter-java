package aunmag.shooter.core.input;

import aunmag.shooter.core.Application;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class Keyboard {

    private final boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

    public Keyboard() {
        Arrays.fill(keys, false);
    }

    public void update() {
        for (int key = 0; key < keys.length; key++) {
            keys[key] = isKeyDown(key);
        }
    }

    public boolean isKeyDown(int key) {
        return Input.isAvailable()
                && GLFW.glfwGetKey(Application.getWindow().id, key) == GLFW.GLFW_TRUE;
    }

    public boolean isKeyPressed(int key) {
        return isKeyDown(key) && !keys[key];
    }

    public boolean isKeyReleased(int key) {
        return !isKeyDown(key) && keys[key];
    }

}
