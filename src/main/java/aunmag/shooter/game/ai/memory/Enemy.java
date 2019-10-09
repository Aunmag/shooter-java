package aunmag.shooter.game.ai.memory;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.graphics.Graphics;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.ai.Ai;
import aunmag.shooter.game.environment.actor.Actor;
import org.lwjgl.opengl.GL11;

public class Enemy extends Destination {

    public final Actor actor;
    public final Destination prediction;

    public Enemy(Ai ai, Actor actor) {
        super(ai, actor.body.position);
        this.actor = actor;
        this.prediction = new Destination(ai, UtilsMath.lead(
                ai.actor.hands.coverage.position,
                ai.actor.kinetics.velocity,
                actor.body.position,
                actor.kinetics.velocity
        ));
    }

    public Enemy(Enemy enemy) {
        this(enemy.ai, enemy.actor);
    }

    @Override
    public void render() {
        prediction.render();

        GL11.glColor4f(1.0f, 0.4f, 0.4f, 0.05f);
        Graphics.draw.circle(
                ai.actor.body.position.x,
                ai.actor.body.position.y,
                ai.getStrategy().dangerZoneRadius,
                true,
                Application.getCamera()::project
        );
    }

    public float getRelativeAngle() {
        return UtilsMath.radiansDifference(actor.body.radians, direction);
    }

}
