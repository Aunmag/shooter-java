package aunmag.shooter.game.ai.memory;

import aunmag.shooter.core.math.CollisionCC;
import aunmag.shooter.core.utilities.Lazy;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.ai.Ai;
import aunmag.shooter.game.environment.actor.Actor;
import org.joml.Vector2f;

public class Enemy extends Destination {

    public final Actor actor;
    public final Lazy<Float> angleRelative = new Lazy<>(this::computeAngleRelative);
    public final Lazy<Boolean> isReached = new Lazy<>(this::computeIsReached);

    public Enemy(Ai ai, Actor actor) {
        super(ai, actor.body.position);
        this.actor = actor;
    }

    @Override
    public void refresh() {
        super.refresh();
        angleRelative.recompute();
        isReached.recompute();
    }

    @Override
    protected Vector2f computePosition() {
        return UtilsMath.lead(
                ai.actor.hands.coverage.position,
                ai.actor.kinetics.velocity,
                actor.body.position,
                actor.kinetics.velocity
        );
    }

    protected float computeAngleRelative() {
        return UtilsMath.radiansDifference(actor.body.radians, direction.get());
    }

    protected boolean computeIsReached() {
        return new CollisionCC(ai.actor.hands.coverage, actor.body).isTrue();
    }

}
