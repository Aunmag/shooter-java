package aunmag.shooter.game.environment.decorations;

import aunmag.shooter.core.math.BodyPoint;
import aunmag.shooter.core.utilities.Operative;

public class Decoration extends Operative {

    public final DecorationType type;
    public final BodyPoint body;

    public Decoration(DecorationType type, float x, float y, float radians) {
        this.type = type;
        body = new BodyPoint(x, y, radians);
    }

    public void render() {
        type.texture.renderOnWorld(
                body.position.x,
                body.position.y,
                body.radians
        );
    }

}
