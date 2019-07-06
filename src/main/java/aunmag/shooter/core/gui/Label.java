package aunmag.shooter.core.gui;

import aunmag.shooter.core.basics.BaseQuad;
import aunmag.shooter.core.gui.font.FontStyle;
import aunmag.shooter.core.gui.font.Text;
import org.joml.Vector4f;

public class Label extends Component {

    private static final int PADDING = 2;
    private Text text;
    public final BaseQuad quad;
    public final BaseQuad screenQuad;

    public Label(int x, int y, int width, int height, String message) {
        this(Grid.GRID_12, x, y, width, height, message, FontStyle.LABEL);
    }

    public Label(
            int x,
            int y,
            int width,
            int height,
            String message,
            FontStyle style
    ) {
        this(Grid.GRID_12, x, y, width, height, message, style);
    }

    public Label(
            Grid grid,
            int x,
            int y,
            int width,
            int height,
            String message,
            FontStyle style
    ) {
        quad = new BaseQuad(x, y, width, height);

        screenQuad = new BaseQuad(
                quad.getPosition().x() * grid.getStepX() + PADDING,
                quad.getPosition().y() * grid.getStepY() + PADDING,
                quad.getWidth() * grid.getStepX() - PADDING * 2,
                quad.getHeight() * grid.getStepY() - PADDING * 2
        );

        text = new Text(0, 0, message, style);
        var textX = screenQuad.getPosition().x() + screenQuad.getCenterX();
        var textY = screenQuad.getPosition().y() + screenQuad.getCenterY();
        text.setPositionCenteredBy(textX, textY);
        text.updateProjection();
    }

    public void render() {
        text.orderRendering();
    }

    @Override
    protected void onRemove() {
        text.remove();
        super.onRemove();
    }

    /* Setters */

    public void setTextColour(Vector4f colour) {
        text.setColour(colour);
    }

}
