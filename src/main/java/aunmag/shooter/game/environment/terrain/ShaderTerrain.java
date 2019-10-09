package aunmag.shooter.game.environment.terrain;

import aunmag.shooter.core.structures.Shader;
import org.joml.Matrix4fc;

public class ShaderTerrain extends Shader {

    private final int uniformLocationProjection;
    private final int uniformLocationQuantity;

    public ShaderTerrain() {
        super(ShaderTerrain.class);

        uniformLocationProjection = getUniformLocation("projection");
        uniformLocationQuantity = getUniformLocation("quantity");

        Shader.setUniform(getUniformLocation("sampler"), 0);
    }

    public void setUniformProjection(Matrix4fc projection) {
        Shader.setUniform(uniformLocationProjection, projection);
    }

    public void setUniformQuantity(int scale) {
        Shader.setUniform(uniformLocationQuantity, scale);
    }

}
