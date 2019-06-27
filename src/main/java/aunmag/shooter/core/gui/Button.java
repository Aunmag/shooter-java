package aunmag.shooter.core.gui;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.graphics.Graphics;
import aunmag.shooter.core.gui.font.FontStyle;
import aunmag.shooter.core.input.Input;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class Button extends Label {

    public static final Runnable ACTION_BACK = Page.STACK::back;
    private static final Vector4f COLOR_DEFAULT = new Vector4f(0.5f, 0.5f, 0.5f, 0.8f);
    private static final Vector4f COLOR_TOUCHED = new Vector4f(0.6f, 0.6f, 0.6f, 0.8f);
    private static final Vector4f COLOR_FONT = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    private static final Vector4f COLOR_FONT_LIGHT = new Vector4f(0.8f, 0.8f, 0.8f, 0.5f);

    private boolean isEnabled = true;
    @Nullable
    private Runnable action;

    public Button(
            int x,
            int y,
            int width,
            int height,
            String text,
            @Nullable Runnable action
    ) {
        this(Grid.GRID_12, x, y, width, height, text, action);
    }

    public Button(
            Grid grid,
            int x,
            int y,
            int width,
            int height,
            String text,
            @Nullable Runnable action
    ) {
        super(grid, x, y, width, height, text, FontStyle.LABEL);
        this.action = action;
    }

    public void update() {
        if (isPressed() && action != null) {
            action.run();
        }
    }

    public void render() {
        setTextColour(isEnabled() ? COLOR_FONT : COLOR_FONT_LIGHT);

        Graphics.draw.withColor(isTouched() ? COLOR_TOUCHED : COLOR_DEFAULT);
        Graphics.draw.quad(
                screenQuad.getPosition().x,
                screenQuad.getPosition().y,
                screenQuad.getPosition().x + screenQuad.getWidth(),
                screenQuad.getPosition().y + screenQuad.getHeight(),
                true,
                Application.getWindow()::project
        );

        super.render();
    }

    /* Setters */

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /* Getters */

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isTouched() {
        return isEnabled() && screenQuad.calculateIsPointInside(
                Input.mouse.position.x,
                Input.mouse.position.y
        );
    }

    public boolean isPressed() {
        return isEnabled()
                && isTouched()
                && Input.mouse.isButtonReleased(GLFW.GLFW_MOUSE_BUTTON_1);
    }

}
