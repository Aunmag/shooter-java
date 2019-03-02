package aunmag.shooter.core.gui;

import aunmag.shooter.core.basics.BaseQuad;
import aunmag.shooter.core.font.FontStyleDefault;
import aunmag.shooter.core.input.Input;
import aunmag.shooter.core.utilities.UtilsGraphics;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class GuiButton extends GuiLabel {

    public static final Runnable actionBack = GuiManager::back;
    private static final Vector4f colorDefault = new Vector4f(0.5f, 0.5f, 0.5f, 0.8f);
    private static final Vector4f colorTouched = new Vector4f(0.6f, 0.6f, 0.6f, 0.8f);
    private static final Vector4f colorFont = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    private static final Vector4f colorFontLight = new Vector4f(0.8f, 0.8f, 0.8f, 0.5f);

    private BaseQuad onScreenQuad;
    private boolean isAvailable = true;
    private boolean isTouched = false;
    private boolean isPressed = false;
    @Nullable private Runnable action;

    public GuiButton(
            int x,
            int y,
            int width,
            int height,
            String text,
            @Nullable Runnable action
    ) {
        this(Grid.GRID_12, x, y, width, height, text, action);
    }

    public GuiButton(
            Grid grid,
            int x,
            int y,
            int width,
            int height,
            String text,
            @Nullable Runnable action
    ) {
        super(grid, x, y, width, height, text, FontStyleDefault.label);
        this.action = action;
        onScreenQuad = calculateOnScreenQuad();
    }

    public void update() {
        if (!isAvailable) {
            return;
        }

        isTouched = onScreenQuad.calculateIsPointInside(
                Input.mouse.position.x,
                Input.mouse.position.y
        );
        isPressed = isTouched && Input.mouse.isButtonReleased(GLFW.GLFW_MOUSE_BUTTON_1);

        if (isPressed && action != null) {
            try {
                action.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void render() {
        Vector4f color = isTouched ? colorTouched : colorDefault;
        UtilsGraphics.setDrawColor(color);
        UtilsGraphics.drawPrepare();
        UtilsGraphics.drawQuad(onScreenQuad, true, false);

        super.render();
    }

    /* Setters */

    public void setIsAvailable(boolean isAvailable) {
        if (isAvailable == this.isAvailable) {
            return;
        } else {
            this.isAvailable = isAvailable;
        }

        if (isAvailable) {
            setTextColour(colorFont);
        } else {
            isTouched = false;
            isPressed = false;
            setTextColour(colorFontLight);
        }
    }

    /* Getters */

    public boolean isPressed() {
        return isPressed;
    }

}
