package aunmag.shooter.core.gui;

import aunmag.shooter.core.gui.font.FontStyle;
import aunmag.shooter.core.utilities.Envelope;
import aunmag.shooter.core.utilities.TimeFlow;
import org.joml.Vector4f;

public class Notification extends Component {

    private final Label title;
    private final Label details;
    private final Envelope opacity;
    private final Vector4f color = new Vector4f(1, 1, 1, 0);

    public Notification(TimeFlow time, String title, String details) {
        this.title = new Label(5, 4, 2, 1, title);
        this.details = new Label(5, 5, 2, 1, details, FontStyle.LABEL_LIGHT);
        this.opacity = new Envelope(0.125f, 2.5f, 0.5f, time); // TODO: Try to use tensity, simplify values
        opacity.start(1);
    }

    @Override
    public void render() {
        opacity.update(); // TODO: Move to update

        color.w = opacity.getValue();

        title.setTextColour(color);
        title.render();

        details.setTextColour(color);
        details.render();
    }

    @Override
    protected void onRemove() {
        title.remove();
        details.remove();
        super.onRemove();
    }

    @Override
    public boolean isActive() {
        return super.isActive() && !opacity.isDone();
    }

}
