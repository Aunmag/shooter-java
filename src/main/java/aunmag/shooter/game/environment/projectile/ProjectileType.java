package aunmag.shooter.game.environment.projectile;

import org.joml.Vector4f;
import org.joml.Vector4fc;

public class ProjectileType {

    public static final Vector4fc color = new Vector4f(1.0f, 0.8f, 0.2f, 1.0f);

    public final int shot;
    public final float mass;
    public final float velocityRecessionFactor;
    public final float size;

    protected ProjectileType(
            int shot,
            float mass,
            float velocityRecessionFactor,
            float size
    ) {
        this.shot = shot;
        this.mass = mass / (float) shot;
        this.velocityRecessionFactor = velocityRecessionFactor;
        this.size = size;
    }

    /* Types */

    public static final ProjectileType laser = new ProjectileType(
            1,
            1f,
            0f,
            0.05f
    );

    public static final ProjectileType _9x18mm_pm = new ProjectileType(
            1,
            6.1f,
            6f,
            0.08f
    );

    public static final ProjectileType _7_62x25mm_tt = new ProjectileType(
            1,
            5.5f,
            6f,
            0.08f
    );

    public static final ProjectileType _12_76_magnum = new ProjectileType(
            16,
            48f,
            4f,
            0.02f
    );

    public static final ProjectileType _5_45x39mm = new ProjectileType(
            1,
            3.4f,
            6f,
            0.08f
    );

    public static final ProjectileType _7_62x54mmR = new ProjectileType(
            1,
            9.6f,
            4.5f,
            0.08f
    );

    public static final ProjectileType _11_48x33mmR = new ProjectileType(
            1,
            15f,
            3f,
            0.1f
    );

}
