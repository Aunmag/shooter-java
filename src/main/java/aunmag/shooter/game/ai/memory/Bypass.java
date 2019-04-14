package aunmag.shooter.game.ai.memory;

import aunmag.shooter.game.ai.Ai;
import org.joml.Vector2f;

public class Bypass extends Destination {

    public final Destination destination;
    public final float offsetAngle;
    public final float offsetDistance;

    public Bypass(
            Ai ai,
            Destination destination,
            float offsetAngle,
            float offsetDistance
    ) {
        super(ai, destination.positionInitial);
        this.destination = destination;
        this.offsetAngle = offsetAngle;
        this.offsetDistance = offsetDistance;
    }

    @Override
    protected Vector2f computePosition() {
        var position = destination.position.get();

        return new Vector2f(
                position.x + offsetDistance * (float) Math.cos(offsetAngle),
                position.y + offsetDistance * (float) Math.sin(offsetAngle)
        );
    }

}
