package aunmag.shooter.game.environment.actor;

import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.client.Context;
import org.joml.Vector2f;

public class ActorStatistics {

    public static final float VELOCITY_AVERAGE_TIME = 0.25f;

    public final Actor actor;
    public final Vector2f velocityAverage = new Vector2f(0, 0);
    public int kills = 0;

    public ActorStatistics(Actor actor) {
        this.actor = actor;
    }

    public void update() {
        updateAverageVelocity();
    }

    private void updateAverageVelocity() {
        var range =
            Context.main.application.frameRate.getFrequency() * VELOCITY_AVERAGE_TIME;
        var velocity = actor.kinetics.velocity;
        velocityAverage.x = UtilsMath.average(velocityAverage.x, range, velocity.x);
        velocityAverage.y = UtilsMath.average(velocityAverage.y, range, velocity.y);
    }

    public void commitKill() {
        kills++;
    }

}
