package aunmag.shooter.game.ai.memory;

import aunmag.shooter.core.math.CollisionCC;
import aunmag.shooter.core.utilities.Lazy;
import aunmag.shooter.core.utilities.UtilsGraphics;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.ai.Ai;
import aunmag.shooter.game.environment.actor.Actor;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

public class Enemy extends Destination {

    public final Actor actor;
    public final Lazy<Vector2f> velocity = new Lazy<>(this::computeVelocity);
    public final Lazy<Float> angleRelative = new Lazy<>(this::computeAngleRelative);
    public final Lazy<Boolean> isReached = new Lazy<>(this::computeIsReached);

    public Enemy(Ai ai, Actor actor) {
        super(ai, actor.body.position);
        this.actor = actor;
    }

    @Override
    public void refresh() {
        super.refresh();
        velocity.recompute();
        angleRelative.recompute();
        isReached.recompute();
    }

    protected Vector2f computeVelocity() {
        var current = new Vector2f(actor.kinetics.velocity);
        var previous = velocity.getRaw();

        if (previous != null) {
            current.add(previous).mul(0.5f);
        }

        return current;
    }

    @Override
    protected Vector2f computePosition() {
        return UtilsMath.lead(
                ai.actor.hands.coverage.position,
                ai.actor.kinetics.velocity,
                actor.body.position,
                velocity.get()
        );
    }

    protected float computeAngleRelative() {
        return UtilsMath.radiansDifference(actor.body.radians, direction.get());
    }

    protected boolean computeIsReached() {
        return new CollisionCC(ai.actor.hands.coverage, actor.body).isTrue();
    }

    @Override
    public void render() {
        super.render();

        GL11.glColor4f(1.0f, 0.4f, 0.4f, 0.05f);
        UtilsGraphics.drawCircle(
                ai.actor.body.position.x,
                ai.actor.body.position.y,
                ai.getStrategy().closeDistanceToEnemy,
                true,
                true
        );
    }

}
