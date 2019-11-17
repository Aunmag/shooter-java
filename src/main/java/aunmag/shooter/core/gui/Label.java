package aunmag.shooter.core.gui;

import aunmag.shooter.core.gui.font.FontStyle;
import aunmag.shooter.core.gui.font.Text;
import aunmag.shooter.core.utilities.Quad;
import org.joml.Vector4f;

public class Label extends Component {

    private static final int PADDING = 2;
    private Text text;
    public final Quad quad;
    public final Quad screenQuad;

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
        quad = new Quad(x, y, width, height);

        screenQuad = new Quad(
                quad.position.x * grid.getStepX() + PADDING,
                quad.position.y * grid.getStepY() + PADDING,
                quad.getSizeX() * grid.getStepX() - PADDING * 2,
                quad.getSizeY() * grid.getStepY() - PADDING * 2
        );

        text = new Text(0, 0, message, style);
        var textX = screenQuad.position.x + screenQuad.getCenterX();
        var textY = screenQuad.position.y + screenQuad.getCenterY();
        text.setPositionCenteredBy(textX, textY);
        text.updateProjection();
    }

    @Override
    public void render() {
        text.orderRendering();
    }

    @Override
    protected void onRemove() {
        text.remove();
        super.onRemove();
    }

    public void setTextColour(Vector4f colour) {
        text.setColour(colour);
    }

}
