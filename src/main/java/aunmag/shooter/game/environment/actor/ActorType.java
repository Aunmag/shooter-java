package aunmag.shooter.game.environment.actor;

import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.game.Config;

public class ActorType {

    @Config public static final float STRENGTH_DEFAULT = 7500;

    public final String name;
    public final float radius;
    public final float weight;
    public final float strength;
    public final float velocity;
    public final float velocityFactorSprint;
    public final float velocityRotation;
    public final float damage;
    public final float reaction;
    public final Texture texture;

    public ActorType(
            String name,
            float radius,
            float weight,
            float strength,
            float velocity,
            float velocityFactorSprint,
            float velocityRotation,
            float damage,
            float reaction
    ) {
        this.name = name;
        this.radius = radius;
        this.weight = weight;
        this.strength = strength;
        this.velocity = velocity;
        this.velocityFactorSprint = velocityFactorSprint;
        this.velocityRotation = velocityRotation;
        this.damage = damage;
        this.reaction = reaction;

        texture = Texture.getOrCreate("actors/" + name + "/image", Texture.Type.SPRITE);
    }

    /* Types */

    public static final ActorType human = new ActorType(
            "human",
            0.225f,
            80_000,
            STRENGTH_DEFAULT,
            2.58f,
            2.76f,
            8,
            STRENGTH_DEFAULT / 16f,
            0.1f
    );

    public static final ActorType humanCowboy = new ActorType(
            "human cowboy",
            human.radius,
            0.9f * human.weight,
            0.8f * human.strength,
            1.1f * human.velocity,
            1.2f * human.velocityFactorSprint,
            1.2f * human.velocityRotation,
            0.9f * human.damage,
            1.2f * human.reaction
    );

    public static final ActorType zombie = new ActorType(
            "zombie",
            human.radius,
            70_000,
            0.4f * human.strength,
            0.4f * human.velocity,
            0.4f * human.velocityFactorSprint,
            0.4f * human.velocityRotation,
            human.strength / 8f,
            0.3f
    );

    public static final ActorType zombieAgile = new ActorType(
            "zombie agile",
            0.8f * zombie.radius,
            40_000,
            0.6f * zombie.strength,
            1.5f * zombie.velocity,
            zombie.velocityFactorSprint,
            2.5f * zombie.velocityRotation,
            0.4f * zombie.damage,
            0.1f
    );

    public static final ActorType zombieHeavy = new ActorType(
            "zombie heavy",
            1.2f * zombie.radius,
            120_000,
            2.0f * zombie.strength,
            0.7f * zombie.velocity,
            zombie.velocityFactorSprint,
            0.7f * zombie.velocityRotation,
            1.8f * zombie.damage,
            0.4f
    );

    // Types, player can use
    public static final ActorType[] playableTypes = {
        human,
        humanCowboy,
    };
}
