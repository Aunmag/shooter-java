package aunmag.shooter.game.client.player;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.graphics.Graphics;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.core.utilities.FluidValue;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.environment.actor.Actor;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class Blackout {

    private final Actor player;
    @Nullable
    private final Texture texture;
    private float healthLast;
    private final FluidValue hurt;
    private final float hurtFactor = 4.0f;
    private final float hurtTimeFadeIn = 0.06f;
    private final float hurtTimeFadeOut = hurtTimeFadeIn * 8;

    public Blackout(Actor player) {
        this.player = player;
        texture = Texture.manager.asCover().provide("images/gui/blackout1600");
        hurt = new FluidValue(player.world.getTime(), hurtTimeFadeIn);
        healthLast = player.getHealth();
    }

    public void render() {
        updateHurt();
        renderRectangle();
        renderBoundaries();
    }

    private void updateHurt() {
        hurt.update();

        float damage = healthLast - player.getHealth();
        healthLast = player.getHealth();

        if (damage > 0) {
            hurt.timer.setDuration(hurtTimeFadeIn);
            hurt.setTarget(damage * hurtFactor + hurt.getTarget());
        }

        if (hurt.getTarget() != 0 && hurt.isTargetReached()) {
            hurt.timer.setDuration(hurtTimeFadeOut);
            hurt.setTarget(0);
        }
    }

    private void renderRectangle() {
        float alphaHurt = hurt.getCurrent();
        float alphaWound = (float) Math.pow(1.0f - player.getHealth(), 3);
        float alpha = alphaHurt + alphaWound - (alphaHurt * alphaWound);
        GL11.glColor4f(0f, 0f, 0f, UtilsMath.limit(alpha, 0, 1));
        Graphics.draw.fill();
    }

    private void renderBoundaries() {
        if (texture != null) {
            var alpha = 1 - player.getHealth() / 1.4f;
            var shader = Application.getShader();
            shader.bind();
            shader.setUniformColour(1, 1, 1, alpha);
            shader.setUniformProjection(Application.getWindow().projection);
            texture.bind();
            texture.render();
        }
    }

}
