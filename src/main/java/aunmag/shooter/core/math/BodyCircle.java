package aunmag.shooter.core.math;

import aunmag.shooter.core.Context;
import aunmag.shooter.core.graphics.Graphics;

public class BodyCircle extends BodyPoint {

    public float radius;

    public BodyCircle(float x, float y, float radians, float radius) {
        super(x, y, radians);
        this.radius = radius;
    }

    @Override
    public void render() {
        super.render();
        Graphics.draw.circle(
                position.x,
                position.y,
                radius,
                true,
                Context.main.getCamera()::project
        );
    }

}
