package aunmag.shooter.core.input;

import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class Keyboard {

    private final Input input;
    private final boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

    public Keyboard(Input input) {
        this.input = input;
        Arrays.fill(keys, false);
    }

    public void update() {
        for (int key = 0; key < keys.length; key++) {
            keys[key] = isKeyDown(key);
        }
    }

    public boolean isKeyDown(int key) {
        return GLFW.glfwGetKey(input.window, key) == GLFW.GLFW_TRUE;
    }

    public boolean isKeyPressed(int key) {
        return isKeyDown(key) && !keys[key];
    }

    public boolean isKeyReleased(int key) {
        return !isKeyDown(key) && keys[key];
    }

}
