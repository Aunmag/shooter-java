package aunmag.shooter.core.gui;

import aunmag.shooter.core.structures.Texture;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

public class Page extends Layer {

    public static final Vector4f BACKGROUND = new Vector4f(0.2f, 0.2f, 0.2f, 0.6f);
    public static final PageStack STACK = new PageStack();

    public Page(@Nullable Texture wallpaper) {
        this.wallpaper = wallpaper;
        this.background = BACKGROUND;
    }

    public void open() {
        STACK.open(this);
    }

}
