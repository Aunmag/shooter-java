package aunmag.shooter.game.ai.memory;

import aunmag.shooter.core.Context;
import aunmag.shooter.core.graphics.Graphics;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.ai.Ai;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

public class Destination extends Record {

    public final Vector2f position;
    public final float direction;

    public Destination(Ai ai, Vector2f position) {
        super(ai);
        this.position = position;
        this.direction = UtilsMath.angle(position, ai.actor.body.position);
    }

    public void render() {
        var a = position;
        var b = ai.actor.body.position;

        GL11.glColor4f(1.0f, 0.4f, 0.4f, 0.4f);
        GL11.glLineStipple(5, (short) 0xAAAA);
        GL11.glEnable(GL11.GL_LINE_STIPPLE);
        Graphics.draw.line(a.x, a.y, b.x, b.y, Context.main.getCamera()::project);
        GL11.glDisable(GL11.GL_LINE_STIPPLE);
    }

}
