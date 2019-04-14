package aunmag.shooter.game.ai.memory;

import aunmag.shooter.core.utilities.Lazy;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.ai.Ai;
import org.joml.Vector2f;

public class Destination extends Record {

    public final Vector2f positionInitial;
    public final Lazy<Vector2f> position = new Lazy<>(this::computePosition);
    public final Lazy<Float> distance = new Lazy<>(this::computeDistance);
    public final Lazy<Float> direction = new Lazy<>(this::computeDirection);

    public Destination(Ai ai, Vector2f position) {
        super(ai);
        this.positionInitial = position;
    }

    @Override
    public void refresh() {
        position.recompute();
        distance.recompute();
        direction.recompute();
    }

    protected Vector2f computePosition() {
        return positionInitial;
    }

    protected float computeDistance() {
        return position.get().distance(ai.actor.body.position);
    }

    protected float computeDirection() {
        return UtilsMath.calculateRadiansBetween(
                position.get().x,
                position.get().y,
                ai.actor.body.position.x,
                ai.actor.body.position.y
        );
    }

}
