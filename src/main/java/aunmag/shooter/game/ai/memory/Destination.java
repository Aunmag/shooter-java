package aunmag.shooter.game.ai.memory;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.graphics.Graphics;
import aunmag.shooter.core.utilities.Lazy;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.ai.Ai;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

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

    public void render() {
        var a = position.get();
        var b = ai.actor.body.position;

        GL11.glColor4f(1.0f, 0.4f, 0.4f, 0.4f);
        GL11.glLineStipple(5, (short) 0xAAAA);
        GL11.glEnable(GL11.GL_LINE_STIPPLE);
        Graphics.draw.line(a.x, a.y, b.x, b.y, Application.getCamera()::project);
        GL11.glDisable(GL11.GL_LINE_STIPPLE);
    }

}
