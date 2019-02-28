package aunmag.shooter.core.structures;

import aunmag.shooter.core.basics.BaseQuad;

public class ShaderStatic extends Shader {

    private static final Model model = Model.createFromQuad(new BaseQuad(2, 2));

    public ShaderStatic(Class resourceClass) {
        super(resourceClass);
    }

    public final void render() {
        bind();
        model.render();
    }

}
