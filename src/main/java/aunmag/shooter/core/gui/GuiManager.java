package aunmag.shooter.core.gui;

import aunmag.shooter.core.input.Input;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public final class GuiManager {

    private static List<GuiPage> pages = new ArrayList<>();
    private static boolean shouldClose = false;

    private GuiManager() {}

    public static void update() {
        shouldClose = false;

        if (Input.keyboard.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            back();
        }

        getCurrentPage().update();
    }

    public static void activate() {
        shouldClose = false;
    }

    public static void back() {
        if (isPageMainOpened()) {
            shouldClose = true;
        } else {
            pages.remove(getLastPageIndex());
        }
    }

    public static void open(GuiPage page) {
        pages.add(page);
        activate();
    }

    public static void render() {
        getCurrentPage().render();
    }

    /* Getters */

    private static int getLastPageIndex() {
        return pages.size() - 1;
    }

    public static GuiPage getCurrentPage() {
        return pages.get(getLastPageIndex());
    }

    public static boolean isPageMainOpened() {
        return getLastPageIndex() == 0;
    }

    public static boolean isShouldClose() {
        return shouldClose;
    }

}
