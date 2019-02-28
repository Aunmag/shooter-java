package aunmag.shooter.core.gui;

import aunmag.shooter.core.basics.BaseGrid;
import aunmag.shooter.core.basics.BaseQuad;
import aunmag.shooter.core.font.FontStyle;
import aunmag.shooter.core.font.FontStyleDefault;
import aunmag.shooter.core.font.Text;
import org.joml.Vector4f;

public class GuiLabel extends BaseQuad {

    private static final int padding = 2;
    private BaseGrid grid;
    private Text text;

    public GuiLabel(int x, int y, int width, int height, String message) {
        this(BaseGrid.grid12, x, y, width, height, message, FontStyleDefault.label);
    }

    public GuiLabel(
            int x,
            int y,
            int width,
            int height,
            String message,
            FontStyle style
    ) {
        this(BaseGrid.grid12, x, y, width, height, message, style);
    }

    public GuiLabel(
            BaseGrid grid,
            int x,
            int y,
            int width,
            int height,
            String message,
            FontStyle style
    ) {
        super(x, y, width, height);
        this.grid = grid;

        BaseQuad onScreenQuad = calculateOnScreenQuad();
        text = new Text(0, 0, message, style);
        text.setPositionCenteredBy(
                onScreenQuad.getPosition().x() + onScreenQuad.getCenterX(),
                onScreenQuad.getPosition().y() + onScreenQuad.getCenterY()
        );
        text.updateProjection();
    }

    protected BaseQuad calculateOnScreenQuad() {
        float x = getPosition().x() * grid.getStepX() + padding;
        float y = getPosition().y() * grid.getStepY() + padding;
        float width = grid.getStepX() * getWidth() - padding * 2;
        float height = grid.getStepY() * getHeight()  - padding * 2;

        return new BaseQuad(x, y, width, height);
    }

    public void render() {
        text.orderRendering();
    }

    public void delete() {
        text.remove();
    }

    /* Setters */

    public void setTextColour(float red, float green, float blue, float alpha) {
        text.setColour(red, green, blue, alpha);
    }

    public void setTextColour(Vector4f colour) {
        text.setColour(colour);
    }

}
