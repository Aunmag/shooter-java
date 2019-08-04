package aunmag.shooter.core.shaders;

import aunmag.shooter.core.structures.Shader;
import org.joml.Matrix4fc;
import org.joml.Vector4fc;

public class ShaderTextured extends Shader {

    private final int uniformLocationProjection;
    private final int uniformLocationColour;

    public ShaderTextured() {
        super(ShaderTextured.class);

        uniformLocationProjection = getUniformLocation("projection");
        uniformLocationColour = getUniformLocation("colour");
    }

    public void setUniformProjection(Matrix4fc projection) {
        Shader.setUniform(uniformLocationProjection, projection);
    }

    public void setUniformColour(Vector4fc colour) {
        Shader.setUniform(uniformLocationColour, colour);
    }

    public void setUniformColour(float red, float green, float blue, float alpha) {
        Shader.setUniform(uniformLocationColour, red, green, blue, alpha);
    }

    public void setUniformColourDefault() {
        setUniformColour(1, 1, 1, 1);
    }

}
