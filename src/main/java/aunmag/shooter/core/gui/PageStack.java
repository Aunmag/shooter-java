package aunmag.shooter.core.gui;

import aunmag.shooter.core.Context;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class PageStack {

    private List<Page> pages = new ArrayList<>();
    @Nullable
    private Runnable onQuit = null;

    public void update() {
        if (Context.main.getInput().keyboard.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            back();
        }

        getCurrent().update();
    }

    public void render() {
        getCurrent().render();
    }

    public void open(Page page) {
        pages.add(page);
    }

    public void back() {
        var index = getCurrentIndex();

        if (index > 0) {
            pages.remove(index);
        } else if (onQuit != null) {
            onQuit.run();
        }
    }

    public void setOnQuit(@Nullable Runnable onQuit) {
        this.onQuit = onQuit;
    }

    private int getCurrentIndex() {
        return pages.size() - 1;
    }

    public Page getCurrent() {
        return pages.get(getCurrentIndex());
    }

}
