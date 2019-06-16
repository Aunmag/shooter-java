package aunmag.shooter.core.graphics;

import org.joml.Vector2f;

@FunctionalInterface
public interface Projector {

    Vector2f project(float x, float y);

}
