package aunmag.shooter.game.environment.terrain;

import aunmag.shooter.core.Application;
import aunmag.shooter.core.graphics.Graphics;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.game.client.Context;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class Terrain {

    private static final int BLOCK_SIZE = 4;

    private final ShaderTerrain shader = new ShaderTerrain();

    @Nullable
    private final Texture texture = Texture.manager
                .asSprite()
                .withModelSize(BLOCK_SIZE, BLOCK_SIZE)
                .provide("images/textures/terrain");

    public void render() {
        if (texture == null || Context.main.isDebug()) {
            renderGrid();
        } else {
            renderTexture();
        }
    }

    private void renderGrid() {
        var camera = Application.getCamera();
        var length = Math.abs(camera.scale / 2);
        var x = camera.getPosition().x;
        var y = camera.getPosition().y;
        var step = 1f;

        GL11.glColor3f(0.4f, 0.4f, 0.4f);

        for (var n = -length; n < length; n += step) {
            var xn = (float) Math.ceil(x + n);
            var yn = (float) Math.ceil(y + n);

            if (xn < x + length) {
                Graphics.draw.line(xn, y - length, xn, y + length, camera::project);
            }

            if (yn < y + length) {
                Graphics.draw.line(x - length, yn, x + length, yn, camera::project);
            }
        }
    }

    private void renderTexture() {
        if (texture == null) {
            return;
        }

        var camera = Application.getCamera();
        var blocks = (int) Math.ceil((camera.scale + BLOCK_SIZE) / BLOCK_SIZE / 2) * 2;
        var projection = camera.toViewProjection(
                round(camera.getPosition().x, BLOCK_SIZE),
                round(camera.getPosition().y, BLOCK_SIZE),
                0
        );

        shader.bind();
        shader.setUniformQuantity(blocks);
        shader.setUniformProjection(projection);

        texture.bind();
        texture.render();
    }

    private float round(float n, float reminder) {
        return (float) Math.round(n / reminder) * reminder;
    }

}
