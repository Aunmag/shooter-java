package aunmag.shooter.game.client.player;

import aunmag.shooter.core.Context;
import aunmag.shooter.core.graphics.Graphics;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.core.utilities.Envelope;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.environment.actor.Actor;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class Blackout {

    private static final float HURT_FACTOR = 4.0f;
    private static final float ENVELOPE_ATTACK = 0.06f;
    private static final float ENVELOPE_RELEASE = 0.48f;

    private final Actor player;
    @Nullable
    private final Texture texture;
    private final Envelope hurt;
    private float healthLast;

    public Blackout(Actor player) {
        this.player = player;
        this.texture = Texture.manager.asCover().provide("images/gui/blackout1600");
        this.hurt = new Envelope(
            ENVELOPE_ATTACK,
            0, // TODO: Try to use sustain
            ENVELOPE_RELEASE,
            player.world.time
        );
        this.healthLast = player.getHealth();
    }

    public void render() {
        updateHurt();
        renderRectangle();
        renderBoundaries();
    }

    private void updateHurt() {
        hurt.update();

        var damage = healthLast - player.getHealth();
        healthLast = player.getHealth();

        if (damage > 0) {
            hurt.start(damage * HURT_FACTOR);
        }
    }

    private void renderRectangle() {
        var alphaHurt = hurt.getValue();
        var alphaWound = (float) Math.pow(1.0f - player.getHealth(), 3);
        var alpha = alphaHurt + alphaWound - (alphaHurt * alphaWound);
        GL11.glColor4f(0f, 0f, 0f, UtilsMath.limit(alpha, 0, 1));
        Graphics.draw.fill();
    }

    private void renderBoundaries() {
        if (texture != null) {
            var alpha = 1 - player.getHealth() / 1.4f;
            var shader = Context.main.getShader();
            shader.bind();
            shader.setUniformColour(1, 1, 1, alpha);
            shader.setUniformProjection(Context.main.getWindow().projection);
            texture.bind();
            texture.render();
        }
    }

}
