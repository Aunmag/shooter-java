package aunmag.shooter.game.ux;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.core.utilities.FluidValue;
import aunmag.shooter.core.utilities.UtilsGraphics;
import aunmag.shooter.core.utilities.UtilsMath;
import aunmag.shooter.game.environment.actor.Actor;
import org.lwjgl.opengl.GL11;

public class Blackout {

    private final Actor actor;
    private final Texture texture;
    private float healthLast = 1.0f;
    private final FluidValue hurt;
    private final float hurtFactor = 4.0f;
    private final float hurtTimeFadeIn = 0.06f;
    private final float hurtTimeFadeOut = hurtTimeFadeIn * 8;

    public Blackout(Actor actor) {
        this.actor = actor;

        texture = Texture.getOrCreate("images/gui/blackout1600",
                                      Texture.Type.STRETCHED
        );
        hurt = new FluidValue(actor.world.getTime(), hurtTimeFadeIn);
    }

    public void render() {
        updateHurt();
        renderRectangle();
        renderBoundaries();
    }

    private void updateHurt() {
        hurt.update();

        float damage = healthLast - actor.getHealth();
        healthLast = actor.getHealth();

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
        float alphaWound = (float) Math.pow(1.0f - actor.getHealth(), 3);
        float alpha = alphaHurt + alphaWound - (alphaHurt * alphaWound);
        GL11.glColor4f(0f, 0f, 0f, UtilsMath.limitNumber(alpha, 0, 1));
        UtilsGraphics.fillScreen();
    }

    private void renderBoundaries() {
        float alpha = 1 - actor.getHealth() / 1.4f;
        Application.getShader().bind();
        Application.getShader().setUniformColour(1, 1, 1, alpha);
        Application.getShader().setUniformProjection(Application.getWindow().projection);
        texture.bind();
        texture.render();
    }
}
